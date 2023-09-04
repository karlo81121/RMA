package com.example.volleyballapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.volleyballapp.databinding.ActivitySignInBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SignInActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.emailSignInLayout)
        val emailTextInputEditText = findViewById<TextInputEditText>(R.id.emailSignIn)
        val passTextInputLayout = findViewById<TextInputLayout>(R.id.passwordSignInLayout)
        val passTextInputEditText = findViewById<TextInputEditText>(R.id.passSignIn)

        passTextInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        val toggleIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_password_toggle_24)

        val coloredToggleIcon = toggleIcon?.let { DrawableCompat.wrap(it) }
        DrawableCompat.setTint(coloredToggleIcon!!, ContextCompat.getColor(this, R.color.black))

        passTextInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, toggleIcon, null)

        passTextInputEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= (passTextInputEditText.right - passTextInputEditText.compoundDrawablesRelative[2].bounds.width())) {
                togglePasswordVisibility(passTextInputEditText)
                return@setOnTouchListener true
            }
            false
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        constraintLayout.setOnClickListener {
            emailTextInputEditText.clearFocus()
            passTextInputEditText.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(constraintLayout.windowToken, 0)
        }

        passTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == ""){
                    passTextInputLayout.isHintEnabled = true
                }
                else {
                    passTextInputLayout.isHintEnabled = false
                }
            }
        })

        emailTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == ""){
                    emailTextInputLayout.isHintEnabled = true
                }
                else {
                    emailTextInputLayout.isHintEnabled = false
                }
            }
        })

        emailTextInputLayout.setEndIconOnClickListener({
            emailTextInputEditText.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(emailTextInputEditText, InputMethodManager.SHOW_IMPLICIT)
        })

        emailTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailTextInputLayout.hint = null
            } else {
                emailTextInputLayout.hint = "Email"
            }
        }

        passTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passTextInputLayout.hint = null
            } else {
                passTextInputLayout.hint = "Password"
            }
        }

        binding.noAccount.setOnClickListener({
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        })

        binding.signInButton.setOnClickListener {
            val email = binding.emailSignIn.text.toString()
            val pass = binding.passSignIn.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            val exception = task.exception
                            if (exception != null) {
                                val errorMessage = when (exception) {
                                    is FirebaseAuthInvalidUserException -> "Wrong email."
                                    is FirebaseAuthInvalidCredentialsException -> "Wrong password."
                                    else -> "Authentication failed: ${exception.message}"
                                }
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun togglePasswordVisibility(textInputEditText: TextInputEditText) {
        isPasswordVisible = !isPasswordVisible

        if (isPasswordVisible) {
            textInputEditText.transformationMethod = null
        } else {
            textInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        textInputEditText.setSelection(textInputEditText.text?.length ?: 0)
    }
}