package com.pallaw.flickrphotos.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by Pallaw Pathak on 21/03/20. - https://www.linkedin.com/in/pallaw-pathak-a6a324a1/
 */
object SearchObservable {
    fun fromView(searchView: EditText): Observable<String> {
        val subject = PublishSubject.create<String>()

        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                subject.onNext(text.toString())
            }
        })
        return subject
    }
}