package com.example.storyapps.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.storyapps.R
import com.example.storyapps.ViewModelFactory
import com.example.storyapps.databinding.FragmentLoginBinding
import com.example.storyapps.service.User
import com.example.storyapps.ui.story.StoryActivity
import com.example.storyapps.utils.UtilsContext.dpToPx
import com.example.storyapps.data.Result

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(context = requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModelObserver()
        playAnimation()
    }


    private fun setupView() {
        binding.apply {
            val editTextEmail = edtInputEmailLogin.editText
            val editTextPassword = edtInputPasswordLogin.editText

            editTextEmail?.id = R.id.edt_login_email
            editTextPassword?.id = R.id.edt_login_password

            editTextEmail?.doAfterTextChanged {
                buttonStatus()
            }

            editTextPassword?.doAfterTextChanged {
                buttonStatus()
            }

            btnLogin.setOnClickListener {
                val email = editTextEmail?.text
                val password = editTextPassword?.text

                val user = User(
                    email = email.toString(), password = password.toString()
                )
                loginViewModel.login(user).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.data, Toast.LENGTH_SHORT).show()
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            btnToRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment))
        }
    }

    private fun setupViewModelObserver() {
        loginViewModel.getUserToken().observe(viewLifecycleOwner) { token ->
            if (token.isNotEmpty()) {
                val intent = Intent(activity, StoryActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun buttonStatus() {
        binding.apply {
            val isEmailError =
                edtInputEmailLogin.editText?.text.isNullOrEmpty() || edtInputEmailLogin.error != null
            val isPasswordError =
                edtInputPasswordLogin.editText?.text.isNullOrEmpty() || edtInputPasswordLogin.error != null
            btnLogin.isEnabled = !(isEmailError || isPasswordError)
            if (btnLogin.isEnabled) {
                btnLogin.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_blue
                    )
                )
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivApp, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.edtInputEmailLogin, View.ALPHA, 1f).setDuration(700)
        val password = ObjectAnimator.ofFloat(binding.edtInputPasswordLogin, View.ALPHA, 1f).setDuration(700)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(700)

        val textRegisterFade = ObjectAnimator.ofFloat(binding.tvToRegister, View.ALPHA, 1f).setDuration(0)
        val textRegisterY = ObjectAnimator.ofFloat(binding.tvToRegister, View.TRANSLATION_Y, requireActivity().dpToPx(40f), 0f).apply {
            duration = 1000
        }
        val buttonRegisterFade = ObjectAnimator.ofFloat(binding.btnToRegister, View.ALPHA, 1f).setDuration(0)
        val buttonRegisterY = ObjectAnimator.ofFloat(binding.btnToRegister, View.TRANSLATION_Y, requireActivity().dpToPx(40f), 0f).apply {
            duration = 1000
        }

        val togetherRegister = AnimatorSet().apply {
            playTogether(
                email,
                password,
                buttonLogin,
                textRegisterFade,
                textRegisterY,
                buttonRegisterFade,
                buttonRegisterY
            )
        }
        AnimatorSet().apply {
            playSequentially(togetherRegister)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}