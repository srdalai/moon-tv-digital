package com.moontvdigital.app.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.moontvdigital.app.R
import com.moontvdigital.app.databinding.FragmentProfileBinding
import com.moontvdigital.app.utilities.PreferenceManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager
    private var isLoggedIn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager.getInstance(requireContext())

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }

        binding.settingsEditProfile.setOnClickListener {
            //Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show()
        }

        binding.settingsChangePwd.setOnClickListener {
            showChangePwdDialog()
        }

        binding.settingsWallet.setOnClickListener {
            //Toast.makeText(requireContext(), "Edit Profile", Toast.LENGTH_SHORT).show()
        }

        binding.settingsPaymentHistory.setOnClickListener {
            //Toast.makeText(requireContext(), "Payment History", Toast.LENGTH_SHORT).show()
        }

        binding.settingsLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Leaving so soon!!!")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            logout()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.create().show()
    }

    private fun showChangePwdDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_passcode, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogView)
        dialog.show()

    }

    private fun logout() {
        binding.progressIndicator.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressIndicator.visibility = View.GONE
            preferenceManager.clearUserPrefs()
            Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            (requireContext() as MainActivity).navigateToHome()
        }, 2000)
    }


    private fun updateUi() {
        if (isLoggedIn) {
            binding.tvFullName.text = preferenceManager.userName
            binding.tvMobileNo.text = preferenceManager.mobileNo

            binding.settingsItemsViewGroup.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        } else {
            binding.settingsItemsViewGroup.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        isLoggedIn = preferenceManager.userId != null
        updateUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}