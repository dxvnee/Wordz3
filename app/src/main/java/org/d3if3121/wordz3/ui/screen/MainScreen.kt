package org.d3if3121.wordz3.ui.screen

import org.d3if3121.wordz3.R


import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3121.wordz3.database.WordsDb

import org.d3if3121.wordz3.model.Words
import org.d3if3121.wordz3.navigation.Screen
import org.d3if3121.wordz3.ui.theme.Wordz3Theme
import org.d3if3121.wordz3.util.SettingsDataStore
import org.d3if3121.wordz3.util.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wordz3Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(rememberNavController())
                }
            }
        }
    }
}

val warnaUngu = Color(0xFF4200DB)
val warnaUnguTua = Color(0xFF3300A8)
val warnaPutih = Color(0xFFFFFFFF)
val warnaHitam = Color(0xFF000000)
val warnaAbu = Color(0xFF5C5C5C)
val warnaMerah = Color(0xFFDF0000)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
    Scaffold (
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                                Image(
                                    painter = painterResource(id = R.drawable.wordzwhite),
                                    contentDescription = "App logo",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(10.dp)
                                )

                            Text(  fontWeight = FontWeight.ExtraBold, text = stringResource(id = R.string.app_name), color = warnaPutih)
                        }

                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = warnaUngu,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    actions = {
                        IconButton(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                dataStore.saveLayout(!showList)
                            }
                        }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (showList) R.drawable.baseline_grid_view_24
                                    else R.drawable.baseline_view_list_24
                                ),
                                contentDescription = stringResource(
                                    if (showList) R.string.grid
                                    else R.string.list
                                ),
                                tint = warnaPutih
                            )
                        }
                    }
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                },
                containerColor = warnaUngu,
                shape = RoundedCornerShape(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),
                    tint = warnaPutih
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            ScreenContent(showList, Modifier.padding(vertical = 10.dp), navController)
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ){

            }
        }
    }
}


@Composable
fun ScreenContent(showList: Boolean, modifier: Modifier, navController: NavHostController){

    val context = LocalContext.current
    val db = WordsDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)

    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()


    if (data.isEmpty()){

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        Column {
            Spacer(modifier = Modifier.width(40.dp))

            Column(modifier = Modifier.padding(start = 5.dp)){
                Text(
                    text = stringResource(R.string.my_words),
                    color = warnaUngu,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,

                    )
            }

            if (showList) {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),

                ) {
                    items(data) {
                        ListItem(kata = it) {
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                    }
                }
            } else {

                LazyVerticalStaggeredGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 8.dp)
                ) {

                    items(data) {
                        GridItem(kata = it) {
                            navController.navigate(Screen.FormUbah.withId(it.id))
                        }
                    }
                }
            }
            print("WOII"+data.get(0).word)
            Text(text = "WOII"+data.get(0).word)
        }

    }
}

@Composable
fun ListItem(kata: Words, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = warnaPutih)
            .padding(vertical = 16.dp, horizontal = 5.dp)
            .clickable { onClick() },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp,))
                .background(color = warnaUngu)
                .padding(horizontal = 20.dp, vertical = 10.dp),

            ) {
            Column(
                modifier = Modifier.weight(1f),

                ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {

                    Text(
                        text = kata.word,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = warnaPutih,
                    )

                    Spacer(modifier = Modifier.width(10.dp)) // Memberikan padding di kiri ikon
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "Add new words",
                        tint = warnaPutih,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = kata.meaning,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = warnaPutih,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End, // Mengatur space antara elemen di dalam row
                        verticalAlignment = Alignment.CenterVertically
                    ){

                    }


                }
                Spacer(modifier = Modifier.height(8.dp))

            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp,))
                .background(color = warnaPutih)
                .padding(vertical = 15.dp, horizontal = 18.dp)

        ){
            Row(

            )
            {
                Text(
                    text = stringResource(R.string.notes),
                    fontSize = 16.sp,
                    color = warnaUnguTua,
                    fontWeight = FontWeight.SemiBold,
                    )

                Text(
                    text = kata.note,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = warnaUnguTua,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = kata.dateAdded,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = warnaHitam
                )

            }

        }

    }
//    ///
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    )
//    {
//        Text(
//            text = kata.word,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            fontWeight = FontWeight.Bold
//        )
//        Text(
//            text = kata.meaning,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//        Text(
//            text = kata.dateAdded,
//        )
//
//    }
}

@Composable
fun GridItem(
    kata: Words,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() },

        colors = CardDefaults.cardColors(
            containerColor = warnaPutih
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp,))
                    .background(color = warnaUngu)
                    .padding(horizontal = 18.dp, vertical = 10.dp),

            ){
                Text(
                    text = kata.word,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,
                    color = warnaPutih,
                    fontSize = 18.sp
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(

                )
                {

                    Text(
                        text = kata.meaning,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = warnaUngu,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = kata.note,
                        color = warnaUngu,
                        fontWeight = FontWeight.Thin,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPredview() {
    Wordz3Theme {
        MainScreen(rememberNavController())
    }
}