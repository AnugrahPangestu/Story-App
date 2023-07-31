package com.example.storyapps.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import androidx.navigation.fragment.findNavController
import com.example.storyapps.R
import com.example.storyapps.ViewModelFactory
import com.example.storyapps.databinding.FragmentRegisterBinding
import com.example.storyapps.service.User
import com.example.storyapps.utils.UtilsContext.dpToPx
import com.example.storyapps.utils.UtilsContext.getScreenWidth
import com.example.storyapps.data.Result

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(context = requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        playAnimation()
    }

    private fun setupView() {
        binding.apply {
            val editTextEmail = edtInputEmailRegister.editText
            val editTextPassword = edtInputPasswordRegister.editText
            val editTextConfirm = edtInputConfirmPassword.editText

            editTextEmail?.id = R.id.edt_register_email
            editTextPassword?.id = R.id.edt_register_password
            editTextConfirm?.id = R.id.edt_confirm_password

            edtRegisterName.doAfterTextChanged {
                buttonStatus()
            }
            editTextEmail?.doAfterTextChanged {
                buttonStatus()
            }
            editTextPassword?.doAfterTextChanged {
                edtInputConfirmPassword.setPassword(it.toString())
                if (it.toString() == editTextConfirm?.text.toString()) {
                    edtInputConfirmPassword.error = null
                }
                buttonStatus()
            }
            editTextConfirm?.doAfterTextChanged {
                buttonStatus()
            }

            btnRegister.setOnClickListener {
                val name: String = edtRegisterName.text.toString()
                val email: String = editTextEmail?.text.toString()
                val password: String = editTextPassword?.text.toString()

                val user = User(name, email, password)
                registerViewModel.register(user).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.data, Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            btnToLogin.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun buttonStatus() {
        binding.apply {
            val isNameError = edtRegisterName.text.isNullOrEmpty()
            val isEmailError =
                edtInputEmailRegister.editText?.text.isNullOrEmpty() || edtInputEmailRegister.error != null
            val isPasswordError =
                edtInputPasswordRegister.editText?.text.isNullOrEmpty() || edtInputPasswordRegister.error != null
            val isConfirmError =
                edtInputConfirmPassword.editText?.text.isNullOrEmpty() || edtInputConfirmPassword.error != null

            btnRegister.isEnabled =
                !(isNameError || isEmailError || isPasswordError || isConfirmError)
            if (btnRegister.isEnabled) {
                btnRegister.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_blue
                    )
                )
            }
        }
    }

    private fun playAnimation() {
        val width = requireActivity().dpToPx(requireActivity().getScreenWidth())

        ObjectAnimator.ofFloat(binding.ivAppRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameFade = ObjectAnimator.ofFloat(binding.edtInputName, View.ALPHA, 1f).setDuration(700)
        val emailFade = ObjectAnimator.ofFloat(binding.edtInputEmailRegister, View.ALPHA, 1f).setDuration(500)
        val passwordFade = ObjectAnimator.ofFloat(binding.edtInputPasswordRegister, View.ALPHA, 1f).setDuration(500)
        val confirmFade = ObjectAnimator.ofFloat(binding.edtInputConfirmPassword, View.ALPHA, 1f).setDuration(500)
        val buttonRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        val textToLoginFade = ObjectAnimator.ofFloat(binding.tvToLogin, View.ALPHA, 1f).setDuration(0)
        val textLoginY = ObjectAnimator.ofFloat(binding.tvToLogin, View.TRANSLATION_Y, requireActivity().dpToPx(40f), 0f).apply {
            duration = 500
        }

        val buttonToLoginFade = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(0)
        val buttonLoginY = ObjectAnimator.ofFloat(binding.btnToLogin, View.TRANSLATION_Y, requireActivity().dpToPx(40f), 0f).apply {
            duration = 500
        }

        val togetherLogin = AnimatorSet().apply {
            playTogether(textToLoginFade, textLoginY, buttonToLoginFade, buttonLoginY)
        }

        val groupInput = AnimatorSet().apply {
            playSequentially(
                nameFade,
                emailFade,
                passwordFade,
                confirmFade,
                buttonRegister,
                togetherLogin
            )
        }

        AnimatorSet().apply {
            playTogether(groupInput)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}