package tr.gov.nuritiras.kullaniciprojesi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tr.gov.nuritiras.kullaniciprojesi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent= Intent(this,DetaActivity::class.java)
            startActivity(intent)
        }
    }

    fun onClickGiris(view: View) {
        val eposta=binding.editTextEmailAddress.text.toString()
        val parola=binding.editTextPassword.text.toString()
        if(eposta.isEmpty() || parola.isEmpty())
        {
            Toast.makeText(this, "Lütfen kad şif boş g", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(eposta, parola)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DetaActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {

                        Toast.makeText(
                            baseContext,
                            "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }

        }
    }
    fun onClickKayit(view: View) {
        val intent= Intent(this,KayitActivity::class.java)
        startActivity(intent)
        finish()
    }
}