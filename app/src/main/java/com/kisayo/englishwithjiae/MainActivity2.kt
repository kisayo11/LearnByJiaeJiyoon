package com.kisayo.englishwithjiae

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieDrawable
import com.kisayo.englishwithjiae.data.WordData2
import com.kisayo.englishwithjiae.databinding.ActivityMain2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.stream.Collectors.toList
import kotlin.random.Random




class MainActivity2 : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private lateinit var tts: TextToSpeech

    private var currentWord: String = ""
    private var currnetAnswer: String = ""
    private var questionIndex: Int = 0
    private var totalQuestions: Int = 10 // 기본값 10
    private val selectedLetters = StringBuilder() // 클릭한 자음/모음을 저장할 StringBuilder
    private val clickedButtons = mutableListOf<Pair<Button, String>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // 스킵된 문제를 기록할 변수
    private val skippedQuestions = mutableSetOf<Int>()

    // 프로그래스 바의 총 개수와 현재 진행상태
    private var maxProgress: Int = 10 // 기본값 10
    private var currentProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // TextToSpeech 초기화
        tts = TextToSpeech(this, this)

        // 버튼 클릭 리스너 설정
        binding.deleteButton.setOnClickListener { deleteLastLetter() }
        binding.skipButton.setOnClickListener { skipToNextWord() }
        binding.listenButton.setOnClickListener { speakCurrentWord() }




        val intent = intent
        maxProgress = intent.getIntExtra("PROGRESS_COUNT", 10)
        totalQuestions = maxProgress
        currentProgress = 0

        showNewWord()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val languageResult = tts.setLanguage(Locale.KOREA)
            if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language is not supported or missing data.")
            }
            tts.setSpeechRate(0.75f)
        } else {
            Log.e("TextToSpeech", "Initialization failed.")
        }
    }

    private fun speakCurrentWord() {
        if (::tts.isInitialized) {
            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    private fun showNewWord() {
        if (questionIndex >= totalQuestions) {
            handleCompletion()
            return
        }

        val randomIndex = Random.nextInt(WordData2.word2.size)
        val words = WordData2.word2[randomIndex]
        currentWord = words
        binding.wordTextView.text = "음성 듣기"

        // 기존 버튼 제거
        binding.buttonsGridLayout.removeAllViews()
        selectedLetters.clear()

        // GridLayout의 열과 행 크기 설정
        val numberOfColumns = 4
        val numberOfRows = 2
        binding.buttonsGridLayout.columnCount = numberOfColumns
        binding.buttonsGridLayout.rowCount = numberOfRows

        // 문자로 버튼을 생성하여 GridLayout에 추가
        val buttonChars = List(8) { ' ' } // 기본적으로 빈 문자로 채운 16개의 버튼
        val shuffledChars = (words.toList() + List(8 - words.length) { ' ' }).shuffled()

        for (i in 0 until 8) {
            val char = shuffledChars[i]
            val button = Button(this).apply {
                text = if (char == ' ') "" else char.toString()
                isEnabled = char != ' ' // 빈 문자 버튼은 비활성화
                setOnClickListener { addLetterToSelection(this, char.toString()) }
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    textSize = 24f
                    typeface = ResourcesCompat.getFont(context, R.font.nanumpen)
                    setMargins(4, 4, 4, 4)
                    rowSpec = GridLayout.spec(i / numberOfColumns, 1f)
                    columnSpec = GridLayout.spec(i % numberOfColumns, 1f)
                }
            }
            binding.buttonsGridLayout.addView(button)
        }

        // 진행 상태 업데이트
        updateProgress()
    }


    private fun addLetterToSelection(button: Button, char: String) {
        selectedLetters.append(char)
        binding.selectedLettersTextView.text = selectedLetters.toString()
        button.isEnabled = false // 클릭한 버튼 비활성화

        // 클릭된 버튼과 문자 쌍을 리스트에 저장
        clickedButtons.add(Pair(button, char))

        // 정답 확인
        if (selectedLetters.toString() == currentWord) {
            questionIndex++
            correctImage()
            showNewWord() // 다음 문제로 넘어가기
        } else if (selectedLetters.length == currentWord.length) {
            // 정답이 아닌 경우
            inCorrectImage()
        }
    }

    private fun deleteLastLetter() {
        if (selectedLetters.isNotEmpty()) {
            val lastChar = selectedLetters.last().toString()
            selectedLetters.deleteCharAt(selectedLetters.length - 1)
            binding.selectedLettersTextView.text=selectedLetters.toString()

                       // 마지막으로 클릭한 버튼을 찾아서 다시 활성화
            val lastClickedButton = clickedButtons.findLast { it.second == lastChar }
            lastClickedButton?.let {
                it.first.isEnabled = true // 버튼을 다시 활성화
                clickedButtons.remove(it) // 리스트에서 제거

            }
        }
    }

    private fun skipToNextWord() {
        questionIndex++
        totalQuestions++ // 문제 수를 하나 증가시킵니다.

        if (questionIndex >= totalQuestions) {
            handleCompletion()
        } else {
            showNewWord()
        }
    }

    private fun updateProgress() {
        binding.progressBar.max = maxProgress
        binding.progressBar.progress = currentProgress
        binding.progressTextView.text = "${currentProgress + 1}/$maxProgress"
    }

    private fun correctImage() {
        binding.correctAnswerIv.visibility = View.VISIBLE
        binding.correctWrongIv.visibility = View.GONE

        // 현재 진행 상태를 증가시키고 업데이트
        currentProgress++
        updateProgress()

        // 현재 진행 상태가 최대값과 같을 경우의 처리
        if (currentProgress >= maxProgress) {
            // 프로그레스 바가 최대값에 도달했을 때, 완료 처리
            handleCompletion()
        } else {
            coroutineScope.launch {
                delay(4000)
                binding.correctAnswerIv.visibility = View.GONE
            }
            showNewWord() // 다음 문제로 넘어가기
        }
    }

    private fun inCorrectImage(){
        binding.correctAnswerIv.visibility = View.GONE
        binding.correctWrongIv.visibility = View.VISIBLE

        coroutineScope.launch {
            delay(3000)
            binding.correctWrongIv.visibility = View.GONE
        }

    }

    private fun handleCompletion() {
        val fireworksAnimationView = binding.fireworksAnimationView
        val restartBtn: Button = binding.restartBtn

        // 애니메이션 뷰의 속도 설정
        val animationSpeed = 0.5f // 필요에 따라 조정
        fireworksAnimationView.setSpeed(animationSpeed)

        // UI 업데이트
        binding.wordTextView.text = "성~~공!"
        binding.buttonsGridLayout.removeAllViews() // 버튼 제거
        binding.selectedLettersTextView.text = ""
        binding.progressBar.progress = maxProgress

        // 애니메이션을 설정합니다.
        fireworksAnimationView.setAnimation(R.raw.fireworks)
        fireworksAnimationView.visibility = View.VISIBLE
        fireworksAnimationView.repeatCount = LottieDrawable.INFINITE
        fireworksAnimationView.playAnimation()

        fireworksAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                restartBtn.visibility = View.VISIBLE // 버튼을 보이도록 설정
            }

            override fun onAnimationEnd(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        restartBtn.setOnClickListener {
            startActivity(Intent(this, Start2::class.java))
        }
    }

}