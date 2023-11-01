package com.moontvdigital.app.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.moontvdigital.app.databinding.FragmentSearchBinding
import com.moontvdigital.app.utilities.Util


class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"
    }

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.editTextSearch.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(textView: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    (requireActivity() as MainActivity).hideKeyboard()
                    performSearch(textView?.text.toString())
                    return true;
                }
                return false;
            }
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class SearchItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val pos = parent.getChildAdapterPosition(view)
            if (pos == 0 || pos % 3 == 0) {
                outRect.set(
                    Util.dpToPx(parent.context, 16), 0,
                    Util.dpToPx(parent.context, 4), 8)
            } else if ((pos + 1) % 3 == 0) {
                outRect.set(
                    Util.dpToPx(parent.context, 4), 0,
                    Util.dpToPx(parent.context, 16), 8)
            } else {
                outRect.set(
                    Util.dpToPx(parent.context, 4), 0,
                    Util.dpToPx(parent.context, 4), 8)
            }
        }
    }

}