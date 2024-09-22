package com.echoist.linkedout.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.colintheshots.twain.MarkdownText
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@OptIn(ExperimentalRichTextApi::class)
@Preview
@Composable
fun t() {
    var isBoldClicked by remember {
        mutableStateOf(false)
    }

    Column {
        val state = rememberRichTextState()

        RichTextEditor(
            state = state,
        )
        Log.d("html 코드로 string 빼기", state.toHtml())
        Log.d("마크다운 코드로 string 빼기", state.toMarkdown())

        state.toHtml()
        Button(onClick = { isBoldClicked = !isBoldClicked }) {
            Text(text = "볼드", color = Color.White)

        }
        state.toMarkdown()
        MarkdownText(markdown = state.toMarkdown(), color = Color.Yellow)
        RichText(state = rememberRichTextState().setMarkdown(state.toMarkdown() /** 여기 마크다운 넣으면됨. **/), color = Color.White)
        RichText(state = rememberRichTextState().setHtml(state.toHtml()/** 여기 html 넣으면됨. **/), color = Color.Blue)

        state.addSpanStyle(SpanStyle(background = Color.Magenta))
        // Toggle a span style.
        if (isBoldClicked) state.addSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) else state.removeSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
        if (isBoldClicked) state.addSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) else state.removeSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
        if (isBoldClicked) state.addSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) else state.removeSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        if (isBoldClicked) state.addSpanStyle(SpanStyle(background = Color.Magenta)) else state.removeSpanStyle(SpanStyle(background = Color.Magenta))

    //if (isBoldClicked) state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center)) else state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))


    }
}
