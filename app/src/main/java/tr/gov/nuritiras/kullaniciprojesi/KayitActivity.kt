package tr.gov.nuritiras.kullaniciprojesi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.gov.nuritiras.kullaniciprojesi.databinding.ActivityKayitBinding
import tr.gov.nuritiras.kullaniciprojesi.databinding.ActivityMainBinding

class KayitActivity : AppCompatActivity() {
    private lateinit var binding:ActivityKayitBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKayitBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth

    }

    fun onClickKaydet(view: View) {

        val eposta=binding.editTextTextEmailAddress.text.toString()
        val parola=binding.editTextTextPassword.text.toString()
        val ad=binding.editTextText.text.toString()
        val no=binding.editTextNumber.text.toString()
        if(eposta.isEmpty() || parola.isEmpty() || ad.isEmpty() || no.isEmpty()){
            Toast.makeText(this,"Lütfen boşlukları doldurunuz",Toast.LENGTH_LONG).show()
        }
        else
        {
            auth.createUserWithEmailAndPassword(eposta, parola)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val user = hashMapOf(
                            "eposta" to eposta,
                            "parola" to parola,
                            "tamad" to ad,
                            "ogrenciNo" to no
                        )

                        db.collection("users").document(userId.toString()).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Firestore kaydı başarısız.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    else
                    {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed." + task.exception,
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}