package com.example.theatre.core.presentation.model

import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.theatre.databinding.LayoutErrorBinding

typealias SuccessStateAction = (content: Any?) -> Unit
typealias ErrorStateAction = (error: ErrorModel) -> Unit
typealias LoadingStateAction = () -> Unit
typealias TryAgainAction = () -> Unit

/**
 * Функция для удобной работы с готовым [ContentResultState] в фрагментах
 *
 * @param onStateSuccess        действие при успехе
 * @param onStateError          действие при неудаче
 * @author Tamerlan Mamukhov on 2023-01-07
 */
fun ContentResultState.handleContents(
    onStateSuccess: SuccessStateAction,
    onStateError: ErrorStateAction,
    onStateLoading: LoadingStateAction? = null
) = when (this) {
    is ContentResultState.Content -> {
        onStateSuccess.invoke(this.content)
    }
    is ContentResultState.Error -> {
        onStateError.invoke(this.error)
    }
    is ContentResultState.Loading -> {
        onStateLoading?.invoke()
    }
}

/**
 * Функция, которая упрощает работу с [ContentResultState]
 *
 * @param onStateSuccess    действие при успехе загрузки данных
 * @param tryAgainAction    бействие при неудаче (напр., повторная загрузка)
 * @param viewToShow        [ViewGroup], которую надо показать после загрузки данных
 * @param progressBar       [ProgressBar], показывающий процесс загрузки
 * @param errorLayout       лайаут с информацией об ошибке
 */
fun ContentResultState.refreshPage(
    onStateSuccess: SuccessStateAction,
    tryAgainAction: TryAgainAction? = null,
    viewToShow: ViewGroup,
    progressBar: ProgressBar,
    errorLayout: LayoutErrorBinding? = null,
) {
    if (this is ContentResultState.Content) {
        viewToShow.isVisible = true
        onStateSuccess.invoke(this.content)
    }


    errorLayout?.root?.isVisible = this is ContentResultState.Error
    if (this is ContentResultState.Error) {
        errorLayout?.apply {
            textErrorTitle.setText(this@refreshPage.error.title)
            textErrorDescription.setText(this@refreshPage.error.description)
            btnErrorTryAgain.setOnClickListener {
                tryAgainAction?.invoke()
            }
        }
    }

    progressBar.isVisible = this is ContentResultState.Loading
}