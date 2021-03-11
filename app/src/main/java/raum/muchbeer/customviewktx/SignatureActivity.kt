package raum.muchbeer.customviewktx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import raum.muchbeer.customviewktx.canvas.MyCanvasView

class SignatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      // setContentView(R.layout.activity_signature)
        val myCanvasView =MyCanvasView(this)

        myCanvasView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)

        setContentView(myCanvasView)
    }
}