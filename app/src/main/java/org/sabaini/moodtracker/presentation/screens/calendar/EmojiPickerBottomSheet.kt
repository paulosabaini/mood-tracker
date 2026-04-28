package org.sabaini.moodtracker.presentation.screens.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sabaini.moodtracker.R

class EmojiPickerBottomSheet(
    private val emojis: List<android.text.SpannableString>,
    private val onEmojiClick: (String) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_emoji_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.emoji_grid)
        recyclerView.layoutManager = GridLayoutManager(context, 5)
        recyclerView.adapter = EmojiAdapter(emojis) { emoji ->
            onEmojiClick(emoji)
            dismiss()
        }
    }

    companion object {
        const val TAG = "EmojiPickerBottomSheet"
    }
}
