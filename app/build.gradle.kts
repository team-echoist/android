import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")    // kapt 플러그인 추가
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")


}
val keystorePropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.echoist.linkedout"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.echoist.linkedout"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        //git ignore 용입니다.
        //구글 네이티브 앱 키
        buildConfigField(
            "String",
            "google_native_api_key",
            keystoreProperties["google_native_app_key"].toString()
        )
        //카카오 네이티브 앱 키
        buildConfigField(
            "String",
            "kakao_native_app_key",
            keystoreProperties["kakao_native_app_key"].toString()
        )
        //네이버 네이티브 clientID
        buildConfigField(
            "String",
            "naver_client_id",
            keystoreProperties["naver_client_id"].toString()
        )
        //네이버 네이티브 clientSecret
        buildConfigField(
            "String",
            "naver_slient_secret",
            keystoreProperties["naver_slient_secret"].toString()
        )
        //네이버 client Name
        buildConfigField(
            "String",
            "naver_client_name",
            keystoreProperties["naver_client_name"].toString()
        )
        resValue("string","kakao_oauth_host",keystoreProperties["kakao_oauth_host"].toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.media3.extractor)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.15.1")

    implementation ("com.google.dagger:hilt-android:2.48")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")
    implementation ("com.github.skydoves:landscapist-glide:1.4.7")


    implementation ("androidx.compose.runtime:runtime-livedata:1.6.5")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("com.kakao.sdk:v2-all:2.19.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation ("com.kakao.sdk:v2-user:2.19.0") // 카카오 로그인
    implementation ("com.kakao.sdk:v2-talk:2.19.0") // 친구, 메시지(카카오톡)
    implementation ("com.kakao.sdk:v2-share:2.19.0") // 메시지(카카오톡 공유)
    implementation ("com.kakao.sdk:v2-friend:2.19.0") // 카카오톡 소셜 피커, 리소스 번들 파일 포함
    implementation ("com.kakao.sdk:v2-navi:2.19.0") // 카카오내비
    implementation ("com.kakao.sdk:v2-cert:2.19.0") // 카카오 인증서비스

    implementation ("com.navercorp.nid:oauth-jdk8:5.9.0") // 네이버 로그인
    implementation("com.colintheshots:twain:0.3.2")

    implementation ("com.google.accompanist:accompanist-pager:0.20.1") //view pager
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.20.1")

    implementation("tech.thdev:extensions-compose-keyboard-state:1.4.0-alpha03")

    implementation("io.github.mr0xf00:easycrop:0.1.1")
    implementation ("io.github.rroohit:ImageCropView:2.2.0")
    implementation(( "com.vanniktech:android-image-cropper:4.5.0" ))

    //동영상 exoplayer
    implementation ("androidx.media3:media3-exoplayer:1.3.1")
    implementation ("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation ("androidx.media3:media3-ui:1.3.1")
    implementation ("androidx.compose.foundation:foundation:1.6.7")

    //splash
    implementation ("androidx.core:core-splashscreen:1.0.1")

    implementation ("androidx.compose.material3:material3:1.3.0-alpha06")

    implementation ("com.google.accompanist:accompanist-flowlayout:0.24.7-alpha")
    implementation ("me.saket.extendedspans:extendedspans:1.3.0")

    //refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")

    implementation ("com.github.BILLyTheLiTTle:LazyColumns:0.2.7")

//    val accompanist_version = "1.7.0"
//    //네비게이션 화면전환 애니메이션
//    implementation ("com.google.accompanist:accompanist-navigation-animation:<$accompanist_version>")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //lottie
    implementation ("com.airbnb.android:lottie-compose:6.4.1")

    val room_version = "2.6.1"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")

    kapt ("org.xerial:sqlite-jdbc:3.34.0")

    //splashscreen api
    implementation ("androidx.core:core-splashscreen:1.0.0-rc01")

    //리치텍스트 compose용 라이브러리 에디터
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc04")

    //브라우저 헬퍼
    implementation ("com.google.androidbrowserhelper:androidbrowserhelper:2.2.0")



}


