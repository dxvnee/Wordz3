package org.d3if3121.wordz3.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3121.wordz3.R
import org.d3if3121.wordz3.database.WordsDb
import org.d3if3121.wordz3.model.Words
import org.d3if3121.wordz3.ui.theme.Wordz3Theme
import org.d3if3121.wordz3.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen2(navController: NavHostController, id: Long? = null){
    var triggerRefresh by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val db = WordsDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)

    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()





    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ){
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.kembali),
                                tint = warnaPutih
                            )
                        }

                    },
                    title = {
                        Text(  fontWeight = FontWeight.ExtraBold, text = stringResource(id = R.string.quiz), color = warnaPutih)
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = warnaUngu,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    actions = {

                    },

                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = warnaUnguTua)
                        .size(6.dp)
                        .padding(20.dp, 20.dp)

                ){

                }

            }
        },



        ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(170.dp))

            Spacer(modifier = Modifier
                .height(70.dp)
                .padding(paddingValues))



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .padding(5.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = warnaPutih)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Column(){
                        QuizApp(navController = navController, data = data)

                    }


                }
            }


        }
    }
}
@Composable
fun QuizApp(navController: NavController, data: List<Words>) {


    var triggerRefresh by remember { mutableStateOf(0) }
    var jumlahSoal by remember { mutableStateOf(1) }
    val correctAnswerIndex = remember { mutableStateOf((0..3).random()) }
    val contextt = LocalContext.current
    var wrongAnswerIndices = (0 until data.size).filter { it != correctAnswerIndex.value }.toMutableList()

    var indexBenar by remember { mutableStateOf(0) }

    var score by remember { mutableStateOf(0) }
    var salah by remember { mutableStateOf(0) }

    var antierror by remember { mutableStateOf(false) }


//    val data = listOf(
//        Words(1, "Kata Satu", "Arti Satu", "Catatan 1", "01/01/2024"),
//        Words(2, "Kata Dua", "Arti Dua", "Catatan 2","02/01/2024"),
//        Words(3, "Kata Tiga", "Arti Tiga", "Catatan 3", "03/01/2024"),
//        Words(4, "Kata Empat", "Arti Empat", "Catatan 4", "04/01/2024"),
//        Words(5, "Kata Lima", "Arti Lima", "Catatan 5", "05/01/2024"),
//    )






    if(data.size >= jumlahSoal) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            print("lewat")
            if (antierror == true){
                indexBenar = 0
                var scoreSekarang = score
                var salahSekarang = salah

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = stringResource(R.string.congratulation),
                        color = warnaUngu,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                            shareData(
                                context = contextt,
                                message = contextt.getString(R.string.bagikan_template,
                                    scoreSekarang.toString(), salahSekarang.toString())
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(50.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = warnaUngu,
                            contentColor = warnaPutih
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.share),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                ){
                    Text(
                        text = stringResource(R.string.correct) + score,
                        color = warnaUngu,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = stringResource(R.string.wrong) + salah,
                            color = warnaMerah,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                    }

                }
            } else {
                Text(
                    text = "No. "+ jumlahSoal,
                    color = warnaUngu,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
//                    text = state.wordsList[indexBenar].words,
                    text = data.get(indexBenar).word,
                    color = warnaUngu,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = stringResource(R.string.choose_answer),
                    color = warnaUngu,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                )
                LazyColumn() {
                    items(4) { index ->
                        val answerText = if (index == correctAnswerIndex.value) {
//                            state.wordsList[indexBenar].meaning
                            data[indexBenar].meaning
                        } else {
                            var wrongIndex = wrongAnswerIndices.random()
                            wrongAnswerIndices.remove(wrongIndex)
                            while(wrongIndex == indexBenar){
                                wrongIndex = wrongAnswerIndices.random()
                            }
//                            state.wordsList[wrongIndex].meaning
                            data[wrongIndex].meaning
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        TombolJawaban(
                            text = answerText,
                            onClick = {
                                if (index == correctAnswerIndex.value) {
                                    indexBenar = tambahIndex(indexBenar)
                                    score++
                                    jumlahSoal++
                                    triggerRefresh++
                                } else {
                                    salah++
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                ){
                    Text(
                        text = stringResource(R.string.correct) + score,
                        color = warnaUngu,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = stringResource(R.string.wrong) + salah,
                            color = warnaMerah,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                    }

                }
            }
        }
    } else {

        if (data.size == jumlahSoal-1 && data.size !=0){
            antierror = true
        }
        jumlahSoal = 1

    }
    LaunchedEffect(triggerRefresh) {

        println("Refresh QuizApp")
        println(jumlahSoal)
        println(data.size)
        println(indexBenar)
        correctAnswerIndex.value = (0..3).random()
        wrongAnswerIndices = (0 until data.size).filter { it != correctAnswerIndex.value }.toMutableList()
        wrongAnswerIndices.shuffle()

    }



}

fun tambahIndex(indexBenar: Int): Int{
    return indexBenar + 1
}

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply{
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}


@Composable
fun TombolJawaban(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .size(50.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = warnaUngu,
            contentColor = warnaPutih
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}




@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    Wordz3Theme {
        QuizScreen2(rememberNavController())
    }
}