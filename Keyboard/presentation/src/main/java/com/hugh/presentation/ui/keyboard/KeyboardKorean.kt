package com.hugh.presentation.ui.keyboard

import android.content.Context
import android.os.*
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import com.hugh.presentation.R
import java.lang.NumberFormatException

class KeyboardKorean constructor(
    var context:Context,
    var layoutInflater: LayoutInflater,
    var keyboardInterationListener: KeyboardInterationListener
){

    lateinit var koreanLayout: LinearLayout
    var isCaps:Boolean = false
    var buttons:MutableList<Button> = mutableListOf<Button>()
    lateinit var hangulMaker: HangulMaker
    var inputConnection:InputConnection? = null
        set(inputConnection){
            field = inputConnection
        }
    val numpadText = listOf<String>("1","2","3","4","5","6","7","8","9","0")
    val firstLineText = listOf<String>("ㅂ","ㅈ","ㄷ","ㄱ","ㅅ","ㅛ","ㅕ","ㅑ","ㅐ","ㅔ")
    val secondLineText = listOf<String>("ㅁ","ㄴ","ㅇ","ㄹ","ㅎ","ㅗ","ㅓ","ㅏ","ㅣ")
    val thirdLineText = listOf<String>("CAPS","ㅋ","ㅌ","ㅊ","ㅍ","ㅠ","ㅜ","ㅡ","DEL")
    val fourthLineText = listOf<String>("!#1","한/영",",","space",".","Enter")
    val firstLongClickText = listOf("!","@","#","$","%","^","&","*","(",")")
    val secondLongClickText = listOf<String>("~","+","-","×","♥",":",";","'","\"")
    val thirdLongClickText = listOf("","_","<",">","/",",","?")
    val myKeysText = ArrayList<List<String>>()
    val myLongClickKeysText = ArrayList<List<String>>()
    val layoutLines = ArrayList<LinearLayout>()
    var downView: View? = null
    var capsView: ImageView? = null

    fun init(){
        koreanLayout = layoutInflater.inflate(com.hugh.presentation.R.layout.keyboard_action, null) as LinearLayout
        hangulMaker = HangulMaker(inputConnection!!)

        val numpadLine = koreanLayout.findViewById<LinearLayout>(
            R.id.numpad_line
        )
        val firstLine = koreanLayout.findViewById<LinearLayout>(
            R.id.first_line
        )
        val secondLine = koreanLayout.findViewById<LinearLayout>(
            R.id.second_line
        )
        val thirdLine = koreanLayout.findViewById<LinearLayout>(
            R.id.third_line
        )
        val fourthLine = koreanLayout.findViewById<LinearLayout>(
            R.id.fourth_line
        )

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
        layoutLines.add(numpadLine)
        layoutLines.add(firstLine)
        layoutLines.add(secondLine)
        layoutLines.add(thirdLine)
        layoutLines.add(fourthLine)
        setLayoutComponents()

    }

    fun getLayout():LinearLayout{
        hangulMaker = HangulMaker(inputConnection!!)
        setLayoutComponents()
        return koreanLayout
    }


    fun modeChange(){
        if(isCaps){
            isCaps = false
            capsView?.setImageResource(R.drawable.ic_caps_unlock)
            for(button in buttons){
                when(button.text.toString()){
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
        }
        else{
            isCaps = true
            capsView?.setImageResource(R.drawable.ic_caps_lock)
            for(button in buttons){
                when(button.text.toString()){
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

    private fun getMyClickListener(actionButton:Button): View.OnClickListener{

        val clickListener = (View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                inputConnection?.requestCursorUpdates(InputConnection.CURSOR_UPDATE_IMMEDIATE)
            }
            val cursorcs:CharSequence? =  inputConnection?.getSelectedText(InputConnection.GET_TEXT_WITH_STYLES)
            if(cursorcs != null && cursorcs.length >= 2){

                val eventTime = SystemClock.uptimeMillis()
                inputConnection?.finishComposingText()
                inputConnection?.sendKeyEvent(
                    KeyEvent(eventTime, eventTime,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                        KeyEvent.FLAG_SOFT_KEYBOARD)
                )
                inputConnection?.sendKeyEvent(
                    KeyEvent(
                        SystemClock.uptimeMillis(), eventTime,
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                        KeyEvent.FLAG_SOFT_KEYBOARD)
                )
                hangulMaker.clear()
            }
            when (actionButton.text.toString()) {

                else -> {
                    try{
                        val myText = Integer.parseInt(actionButton.text.toString())
                        hangulMaker.directlyCommit()
                        inputConnection?.commitText(actionButton.text.toString(), 1)
                    }catch (e: NumberFormatException){
                        hangulMaker.commit(actionButton.text.toString().toCharArray().get(0))
                    }
                    if(isCaps){
                        modeChange()
                    }
                }
            }
        })
        actionButton.setOnClickListener(clickListener)
        return clickListener
    }

    fun getOnTouchListener(clickListener: View.OnClickListener): View.OnTouchListener{
        val handler = Handler()
        val initailInterval = 500
        val normalInterval = 100
        val handlerRunnable = object: Runnable{
            override fun run() {
                handler.postDelayed(this, normalInterval.toLong())
                clickListener.onClick(downView)
            }
        }
        val onTouchListener = object: View.OnTouchListener {
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                when (motionEvent?.getAction()) {
                    MotionEvent.ACTION_DOWN -> {
                        handler.removeCallbacks(handlerRunnable)
                        handler.postDelayed(handlerRunnable, initailInterval.toLong())
                        downView = view!!
                        clickListener.onClick(view)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        handler.removeCallbacks(handlerRunnable)
                        downView = null
                        return true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        handler.removeCallbacks(handlerRunnable)
                        downView = null
                        return true
                    }
                }
                return false
            }
        }

        return onTouchListener
    }

    private fun setLayoutComponents(){
        for(line in layoutLines.indices){
            val children = layoutLines[line].children.toList()
            val myText = myKeysText[line]
            var longClickIndex = 0
            for(item in children.indices){
                val actionButton = children[item].findViewById<Button>(R.id.key_button)
                val spacialKey = children[item].findViewById<ImageView>(R.id.spacial_key)
                var myOnClickListener: View.OnClickListener? = null
                when(myText[item]){
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
                    "한/영" -> {
                        actionButton.text = myText[item]
                        buttons.add(actionButton)
                        myOnClickListener = object : View.OnClickListener{
                            override fun onClick(p0: View?) {
                                keyboardInterationListener.modechange(0)
                            }
                        }
                        actionButton.setOnClickListener(myOnClickListener)
                    }
                    "!#1" -> {
                        actionButton.text = myText[item]
                        buttons.add(actionButton)
                        myOnClickListener = object : View.OnClickListener{
                            override fun onClick(p0: View?) {
                                keyboardInterationListener.modechange(2)
                            }
                        }
                        actionButton.setOnClickListener(myOnClickListener)
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
    fun getSpaceAction(): View.OnClickListener{
        return View.OnClickListener{
            hangulMaker.commitSpace()
        }
    }

    fun getDeleteAction(): View.OnClickListener{
        return View.OnClickListener{
            val cursorcs:CharSequence? =  inputConnection?.getSelectedText(InputConnection.GET_TEXT_WITH_STYLES)
            if(cursorcs != null && cursorcs.length >= 2){

                val eventTime = SystemClock.uptimeMillis()
                inputConnection?.finishComposingText()
                inputConnection?.sendKeyEvent(
                    KeyEvent(eventTime, eventTime,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                        KeyEvent.FLAG_SOFT_KEYBOARD)
                )
                inputConnection?.sendKeyEvent(
                    KeyEvent(
                        SystemClock.uptimeMillis(), eventTime,
                        KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                        KeyEvent.FLAG_SOFT_KEYBOARD)
                )
                hangulMaker.clear()
            }
            else{
                hangulMaker.delete()
            }
        }
    }

    fun getCapsAction(): View.OnClickListener{
        return View.OnClickListener{
            modeChange()
        }
    }

    fun getEnterAction(): View.OnClickListener{
        return View.OnClickListener{
            hangulMaker.directlyCommit()
            val eventTime = SystemClock.uptimeMillis()
            inputConnection?.sendKeyEvent(
                KeyEvent(eventTime, eventTime,
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                    KeyEvent.FLAG_SOFT_KEYBOARD)
            )
            inputConnection?.sendKeyEvent(
                KeyEvent(
                    SystemClock.uptimeMillis(), eventTime,
                    KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0, 0, 0, 0,
                    KeyEvent.FLAG_SOFT_KEYBOARD)
            )
        }
    }


}