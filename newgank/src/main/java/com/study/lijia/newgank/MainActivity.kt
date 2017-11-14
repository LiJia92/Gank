package com.study.lijia.newgank

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.study.lijia.internallibrary.InternalClass
import com.study.lijia.mylibrary.MyClass
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message.text = "哈哈哈"
        message.setOnClickListener {
//            val person = Person("李佳", 25)
//            toast(person.toString())
            toast(MyClass().myClass)
            toast(MyClass().contentFromInternal)
            toast(InternalClass().internalClass)
        }

        doAsync {
            Request("http://gank.io/api/day/2015/08/06").run()
            uiThread { longToast("Request performed") }
        }
    }

    private fun toast(msg: String, tag: String = javaClass.simpleName, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, "[$tag] $msg", length).show()
    }
}
