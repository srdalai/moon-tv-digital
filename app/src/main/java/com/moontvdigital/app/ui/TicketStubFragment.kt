package com.moontvdigital.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moontvdigital.app.data.ShowDate
import com.moontvdigital.app.data.ShowTime
import com.moontvdigital.app.databinding.FragmentTicketStubBinding

class TicketStubFragment(
    private val hallName: String,
    private val curShowDate: ShowDate,
    private val showTime: ShowTime,
    private val walletBalance: String?
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "TicketStubFragment"
    }

    constructor(hallName: String, curShowDate: ShowDate, showTime: ShowTime) : this(
        hallName,
        curShowDate,
        showTime,
        null
    )

    private var _binding: FragmentTicketStubBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(R.style.TicketDialog, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTicketStubBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ticketView.tvTheaterName.text = hallName
        binding.ticketView.tvMovieName.text = showTime.filmName
        binding.ticketView.tvShowDate.text = curShowDate.getShowDate()
        binding.ticketView.tvShowTime.text = showTime.showStartTime
        binding.ticketView.tvTicketPrice.text = "₹" + showTime.ticketRate.toString()
        binding.ticketView.tvWalletBal.text = "Wallet Balance: \u20B9$walletBalance"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}