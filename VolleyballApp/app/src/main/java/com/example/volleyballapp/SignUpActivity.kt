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
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.volleyballapp.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isPasswordVisible = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.emailSignUpLayout)
        val emailTextInputEditText = findViewById<TextInputEditText>(R.id.emailSignUp)
        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.passwordSignUpLayout)
        val passwordTextInputEditText = findViewById<TextInputEditText>(R.id.passwordSignUp)
        val confirmPasswordTextInputLayout = findViewById<TextInputLayout>(R.id.confirmPasswordSignUpLayout)
        val confirmPasswordTextInputEditText = findViewById<TextInputEditText>(R.id.confirmPasswordSignUp)

        passwordTextInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        confirmPasswordTextInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        val toggleIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_password_toggle_24)

        val coloredToggleIcon = toggleIcon?.let { DrawableCompat.wrap(it) }
        DrawableCompat.setTint(coloredToggleIcon!!, ContextCompat.getColor(this, R.color.black))

        passwordTextInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, toggleIcon, null)
        confirmPasswordTextInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, toggleIcon, null)

        passwordTextInputEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= (passwordTextInputEditText.right - passwordTextInputEditText.compoundDrawablesRelative[2].bounds.width())) {
                togglePasswordVisibility(passwordTextInputEditText)
                return@setOnTouchListener true
            }
            false
        }

        confirmPasswordTextInputEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= (confirmPasswordTextInputEditText.right - confirmPasswordTextInputEditText.compoundDrawablesRelative[2].bounds.width())) {
                togglePasswordVisibility(confirmPasswordTextInputEditText)
                return@setOnTouchListener true
            }
            false
        }

        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        constraintLayout.setOnClickListener {
            emailTextInputEditText.clearFocus()
            passwordTextInputEditText.clearFocus()
            confirmPasswordTextInputEditText.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(constraintLayout.windowToken, 0)
        }

        confirmPasswordTextInputEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == ""){
                    confirmPasswordTextInputLayout.isHintEnabled = true
                }
                else {
                    confirmPasswordTextInputLayout.isHintEnabled = false
                }
            }
        })

        passwordTextInputEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString() == ""){
                    passwordTextInputLayout.isHintEnabled = true
                }
                else {
                    passwordTextInputLayout.isHintEnabled = false
                }
            }
        })

        emailTextInputEditText.addTextChangedListener(object : TextWatcher{
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

        passwordTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordTextInputLayout.hint = null
            } else {
                passwordTextInputLayout.hint = "Password"
            }
        }

        confirmPasswordTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                confirmPasswordTextInputLayout.hint = null
            } else {
                confirmPasswordTextInputLayout.hint = "Confirm Password"
            }
        }

        binding.alreadyRegistered.setOnClickListener({
            var intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        binding.signUpButton.setOnClickListener {
            val email = binding.emailSignUp.text.toString()
            val pass = binding.passwordSignUp.text.toString()
            val confirmPass = binding.confirmPasswordSignUp.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            } else {
                                val exception = task.exception
                                if (exception != null) {
                                    val errorMessage = when (exception) {
                                        is FirebaseAuthWeakPasswordException -> "Weak password."
                                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                                        is FirebaseAuthUserCollisionException -> "User with this email already exists."
                                        else -> "Sign up failed: ${exception.message}"
                                    }
                                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords are not matching!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun togglePasswordVisibility(textInputEditText: TextInputEditText) {
        isPasswordVisible = !isPasswordVisible

        // Toggle password visibility and update the compound drawable (icon)
        if (isPasswordVisible) {
            textInputEditText.transformationMethod = null
        } else {
            textInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        // Reposition the cursor to the end of the text
        textInputEditText.setSelection(textInputEditText.text?.length ?: 0)
    }
}