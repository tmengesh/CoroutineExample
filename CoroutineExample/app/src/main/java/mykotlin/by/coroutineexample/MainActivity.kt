package mykotlin.by.coroutineexample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

            //CoroutineScopes: IO (input out put, network request, request to local database),
            // Main(doing work on a main thread), Default(any heavy computational work)
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }


        }

    }
    private fun setNewText (input: String) {
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }
    private suspend fun setTextOnMainThread(input: String) {
        //This will fireup Coroutine and do the job on main thread
        //With this logic we're not freezing the UI as text field value is updated on the main thread
        withContext(Main) {
            setNewText(input)
        }
    }
    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromApi()
        println("debug: $result1")
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi()
        setTextOnMainThread(result2)

    }

    //Asynchronous getResult1FromApi function
    private suspend fun getResult1FromApi(): String{
        logThread(methodname = "getResult1FromApi")
        delay(timeMillis = 1000)
        return RESULT_1

    }
    private suspend fun getResult2FromApi(): String {
        logThread(methodname = "getResult2FromApi")
        delay(timeMillis = 1000)
        return RESULT_2
    }
    private fun logThread(methodname: String) {
        println("debug ${methodname}: ${Thread.currentThread().name}")
    }
}
