package com.hugh.presentation.ui.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.hugh.presentation.R
import com.hugh.presentation.action.keyboard.KeyBoardActor
import com.hugh.presentation.databinding.KeyboardKoreanBinding
import kotlinx.coroutines.*
import java.lang.Runnable

class KeyboardKorean constructor(
    var layoutInflater: LayoutInflater,
    private var inputConnection: InputConnection?
) : PopupMenu.OnMenuItemClickListener  {

    private lateinit var keyboardKoreanBinding: KeyboardKoreanBinding
    private val keyboard = Keyboard()
    private val keyBoardActor by lazy { KeyBoardActor(keyboard) }
    private var isCaps: Boolean = false
    private var buttons: MutableList<Button> = mutableListOf()
    private val hangulUtil: HangulUtil by lazy { HangulUtil() }

    private val numpadText = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    private val firstLineText = listOf("ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ", "ㅐ", "ㅔ")
    private val secondLineText = listOf("ㅁ", "ㄴ", "ㅇ", "ㄹ", "ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ")
    private val thirdLineText = listOf("CAPS", "ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ", "DEL")
    private val fourthLineText = listOf("FAV", "한/영", ",", "space", ".", "Enter")
    private val firstLongClickText = listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")")
    private val secondLongClickText = listOf("~", "+", "-", "×", "♥", ":", ";", "'", "\"")
    private val thirdLongClickText = listOf("", "_", "<", ">", "/", ",", "?")
    private val myKeysText = ArrayList<List<String>>()
    private val myLongClickKeysText = ArrayList<List<String>>()
    private val layoutLines = ArrayList<LinearLayout>()
    private var downView: View? = null
    private var capsView: ImageView? = null

    fun init() {
        keyboardKoreanBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.keyboard_korean, null, false)
        keyboardKoreanBinding.actor = keyBoardActor

        myKeysText.clear()
        myKeysText.add(numpadText)
        myKeysText.add(firstLineText)
        myKeysText.add(secondLineText)
        myKeysText.add(thirdLineText)
        myKeysText.add(fourthLineText)

        myLongClickKeysText.clear()
        myLongClickKeysText.add(firstLongClickText)
        myLongClickKeysText.add(secondLongClickText)
        myLongClickKeysText.add(thirdLongClickText)

        layoutLines.clear()
        layoutLines.add(keyboardKoreanBinding.numpadLine)
        layoutLines.add(keyboardKoreanBinding.firstLine)
        layoutLines.add(keyboardKoreanBinding.secondLine)
        layoutLines.add(keyboardKoreanBinding.thirdLine)
        layoutLines.add(keyboardKoreanBinding.fourthLine)
        setLayoutComponents()

    }

    fun getLayout(): View {
        setLayoutComponents()
        return keyboardKoreanBinding.root
    }

    private fun changeMode() {
        if (isCaps) {
            isCaps = false
            capsView?.setImageResource(R.drawable.ic_caps_unlock)
            for (button in buttons) {
                when (button.text.toString()) {
                    "ㅃ" -> {
                        button.text = "ㅂ"
                    }
                    "ㅉ" -> {
                        button.text = "ㅈ"
                    }
                    "ㄸ" -> {
                        button.text = "ㄷ"
                    }
                    "ㄲ" -> {
                        button.text = "ㄱ"
                    }
                    "ㅆ" -> {
                        button.text = "ㅅ"
                    }
                    "ㅒ" -> {
                        button.text = "ㅐ"
                    }
                    "ㅖ" -> {
                        button.text = "ㅔ"
                    }
                }
            }
        } else {
            isCaps = true
            capsView?.setImageResource(R.drawable.ic_caps_lock)
            for (button in buttons) {
                when (button.text.toString()) {
                    "ㅂ" -> {
                        button.text = "ㅃ"
                    }
                    "ㅈ" -> {
                        button.text = "ㅉ"
                    }
                    "ㄷ" -> {
                        button.text = "ㄸ"
                    }
                    "ㄱ" -> {
                        button.text = "ㄲ"
                    }
                    "ㅅ" -> {
                        button.text = "ㅆ"
                    }
                    "ㅐ" -> {
                        button.text = "ㅒ"
                    }
                    "ㅔ" -> {
                        button.text = "ㅖ"
                    }
                }
            }
        }
    }

    private fun getMyClickListener(actionButton: Button): View.OnClickListener {

        val clickListener = (View.OnClickListener {

            val text = actionButton.text.toString()
            inputConnection?.let {
                hangulUtil.updateLetter(it, text)
            }
        })
        actionButton.setOnClickListener(clickListener)
        return clickListener
    }

    fun getOnTouchListener(clickListener: View.OnClickListener): View.OnTouchListener {
        /*val job = GlobalScope.launch(Dispatchers.Default) {
            while (isActive) {
                clickListener.onClick(downView)
                delay(100)
            }
        }*/

        val handler = Handler()
        val initailInterval = 500
        val normalInterval = 100
        val handlerRunnable = object : Runnable {
            override fun run() {
                handler.postDelayed(this, normalInterval.toLong())
                clickListener.onClick(downView)
            }
        }
        val onTouchListener = object : View.OnTouchListener {
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        handler.removeCallbacks(handlerRunnable)
                        handler.postDelayed(handlerRunnable, initailInterval.toLong())
                        //job.cancel()
                        //job

                        downView = view!!
                        clickListener.onClick(view)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        handler.removeCallbacks(handlerRunnable)
                        //job.cancel()

                        downView = null
                        return true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        handler.removeCallbacks(handlerRunnable)
                        //job.cancel()

                        downView = null
                        return true
                    }
                }
                return false
            }
        }

        return onTouchListener
    }

    private fun setLayoutComponents() {
        for (line in layoutLines.indices) {
            val children = layoutLines[line].children.toList()
            val myText = myKeysText[line]
            for (item in children.indices) {
                val actionButton = children[item].findViewById<Button>(R.id.key_button)
                val spacialKey = children[item].findViewById<ImageView>(R.id.spacial_key)
                var myOnClickListener: View.OnClickListener?
                when (myText[item]) {
                    "FAV" -> {
                        spacialKey.setImageResource(R.drawable.ic_zam)
                        spacialKey.visibility = View.VISIBLE
                        actionButton.visibility = View.GONE
                        myOnClickListener = popupAction()
                        spacialKey.setOnClickListener(myOnClickListener)
                        spacialKey.setOnTouchListener(getOnTouchListener(myOnClickListener))
                        spacialKey.setBackgroundResource(R.drawable.key_background)
                    }

                    "space" -> {
                        spacialKey.setImageResource(R.drawable.ic_space_bar)
                        spacialKey.visibility = View.VISIBLE
                        actionButton.visibility = View.GONE
                        myOnClickListener = getSpaceAction()
                        spacialKey.setOnClickListener(myOnClickListener)
                        spacialKey.setOnTouchListener(getOnTouchListener(myOnClickListener))
                        spacialKey.setBackgroundResource(R.drawable.key_background)
                    }
                    "DEL" -> {
                        spacialKey.setImageResource(R.drawable.ic_baseline_close_24)
                        spacialKey.visibility = View.VISIBLE
                        actionButton.visibility = View.GONE
                        myOnClickListener = getDeleteAction()
                        spacialKey.setOnClickListener(myOnClickListener)
                        spacialKey.setOnTouchListener(getOnTouchListener(myOnClickListener))
                    }
                    "CAPS" -> {
                        spacialKey.setImageResource(R.drawable.ic_caps_unlock)
                        spacialKey.visibility = View.VISIBLE
                        actionButton.visibility = View.GONE
                        capsView = spacialKey
                        myOnClickListener = getCapsAction()
                        spacialKey.setOnClickListener(myOnClickListener)
                        spacialKey.setBackgroundResource(R.drawable.key_background)
                    }
                    "Enter" -> {
                        spacialKey.setImageResource(R.drawable.ic_enter)
                        spacialKey.visibility = View.VISIBLE
                        actionButton.visibility = View.GONE
                        myOnClickListener = getEnterAction()
                        spacialKey.setOnClickListener(myOnClickListener)
                        spacialKey.setOnTouchListener(getOnTouchListener(myOnClickListener))
                        spacialKey.setBackgroundResource(R.drawable.key_background)
                    }
                    else -> {
                        actionButton.text = myText[item]
                        buttons.add(actionButton)
                        myOnClickListener = getMyClickListener(actionButton)
                        actionButton.setOnTouchListener(getOnTouchListener(myOnClickListener))
                    }
                }
                children[item].setOnClickListener(myOnClickListener)
            }
        }
    }

    private fun popupAction(): View.OnClickListener {
        return View.OnClickListener {
            var popup = PopupMenu(it.context, it)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
            popup.setOnMenuItemClickListener(this)
            popup.show()
        }
    }

    private fun getSpaceAction(): View.OnClickListener {
        return View.OnClickListener {
            inputConnection?.let {
                hangulUtil.updateLetter(it, " ")
            }
        }
    }

    private fun getDeleteAction(): View.OnClickListener {
        return View.OnClickListener {
            inputConnection?.let {
                hangulUtil.delete(it)
            }
        }
    }

    private fun getCapsAction(): View.OnClickListener {
        return View.OnClickListener {
            changeMode()
        }
    }

    private fun getEnterAction(): View.OnClickListener {
        return View.OnClickListener {
            inputConnection?.let {
                hangulUtil.updateLetter(it, "\n")
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item1 -> Log.d("TAG", "onMenuItemClick: 앜1")
            R.id.item2 -> Log.d("TAG", "onMenuItemClick: 앜2")
            R.id.item3 -> Log.d("TAG", "onMenuItemClick: 앜3")
            R.id.item4 -> Log.d("TAG", "onMenuItemClick: 앜4")
        }
        return item != null
    }
}