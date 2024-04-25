package com.echoist.linkedout.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SocialLoginViewModel : ViewModel() {
    private val auth : FirebaseAuth = Firebase.auth
    var state = mutableStateOf(false)
    val userName = auth.currentUser?.displayName.toString()

    fun signInWithGoogle(
        launcher: ManagedActivityResultLauncher<Intent,androidx.activity.result.ActivityResult>,
        context: Context
    ){
        val token = BuildConfig.google_native_api_key //토큰값 -> local.properties 통해 git ignore

        // Google 로그인을 구성합니다.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    fun handleGoogleSignInResult(data: Intent?, navController: NavController) {
        // Google ID 토큰을 사용하여 Firebase에 인증합니다.
        try {
            // Google 로그인이 성공하면 Firebase로 인증합니다.
            val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        state.value = true
                        // 로그인 성공
                        navController.navigate("screen2")
                        Log.d("TAG", "Google Sign In Success")
                        Log.d(TAG, "firebaseAuthWithGoogle id:" + account.id)
                        Log.d(TAG, "firebaseAuthWithGoogle idtoken:" + account.idToken) //토큰값.
                        Log.d(TAG, "firebaseAuthWithGoogle current user:" + auth.currentUser!!.displayName.toString()) //회원 이름
                        Log.d(TAG, "firebaseAuthWithGoogle current user:" + auth.currentUser!!.photoUrl.toString()) //회원 사진 Url

                        // 추가 작업 수행
                    } else {
                        // 로그인 실패
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        // 실패 처리 작업 수행
                    }
                }

        }catch (e: ApiException) { // sha-1 또는 app key가 잘못된 경우 로그확인. dialog메세지 띄워줄수있음
                Log.w("TAG", "GoogleSign in Failed", e)
        }
    }



}

//로그인 성공시 일단 간단한 토스트메세지 띄움
@Composable
fun GoogleLoginSuccessDialog(dialogState: MutableState<Boolean>){
    AlertDialog(
        onDismissRequest = { dialogState.value = false},
        confirmButton = {
            Button(onClick = { dialogState.value = false },) {
                Text(text = "확인")
            }
        },
        title = { Text(text = "구글 로그인")},
    )
}