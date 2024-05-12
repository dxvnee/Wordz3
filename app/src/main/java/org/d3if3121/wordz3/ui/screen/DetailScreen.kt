package org.d3if3121.wordz3.ui.screen

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3121.wordz3.R
import org.d3if3121.wordz3.database.WordsDb
import org.d3if3121.wordz3.navigation.Screen
import org.d3if3121.wordz3.ui.theme.Wordz3Theme
import org.d3if3121.wordz3.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar

const val KEY_ID_CATATAN = "idCatatan"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {

    val context = LocalContext.current
    val db = WordsDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    val viewModel2: MainViewModel = viewModel(factory = factory)

    val data by viewModel2.data.collectAsState()

    var word by rememberSaveable { mutableStateOf("") }
    var meaning by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }

    var wordsError by rememberSaveable { mutableStateOf(false) }
    var meaningError by rememberSaveable { mutableStateOf(false) }
    var notesError by rememberSaveable { mutableStateOf(false) }


    var showDialog by remember { mutableStateOf(false) }




    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect
        val data = viewModel.getWords(id) ?: return@LaunchedEffect
        word = data.word
        meaning = data.meaning
        note = data.note
    }

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
                        if(id == null)
                            Text(color = warnaPutih, fontWeight = FontWeight.Bold, text = stringResource(id = R.string.tambah_catatan))
                        else
                            Text(color = warnaPutih, fontWeight = FontWeight.Bold, text = stringResource(id = R.string.edit_catatan))
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = warnaUngu,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                if (data.size < 5) {
                                    Toast.makeText(
                                        context,
                                        R.string.minimal_5,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    navController.navigate(Screen.Quiz.route)
                                }
                            }
                        ){
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = stringResource(R.string.simpan),
                                tint = warnaPutih
                            )
                        }
                        if (id != null){
                            DeleteAction {
                                showDialog = true
                            }
                            DisplayAlertDialog(
                                openDialog = showDialog,
                                onDismissRequest = {
                                    showDialog = false
                                }
                            ) {
                                showDialog = false
                                viewModel.delete(id)
                                navController.popBackStack()
                            }
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
        }
    ) { padding ->
        FormCatatan(
            modifier = Modifier.padding(padding),
            word = word,
            onWordChange = {
                word = it
            },
            meaning = meaning,
            onMeaningChange = {
                meaning = it
            },
            note = note,
            onNoteChange = {
                note = it
            },
            isChecked = isChecked,
            onCheckedChange = { isChecked = it },


            id = id,
            viewModel = viewModel,
            navController = navController
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCatatan(

    modifier: Modifier,
    word: String,
    onWordChange: (String) -> Unit,

    meaning: String,
    onMeaningChange: (String) -> Unit,

    note: String,
    onNoteChange: (String) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,



    id: Long?,

    viewModel: DetailViewModel,
    navController: NavHostController

) {
    var wordsError by rememberSaveable { mutableStateOf(false) }
    var meaningError by rememberSaveable { mutableStateOf(false) }
    var notesError by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = stringResource(R.string.add_new_words),
            color = warnaUngu,
            fontSize = 23.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 5.dp)
        )

        Spacer(
            modifier = Modifier
                .height(70.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(11.dp))
                .padding(5.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = warnaPutih)
                    .padding(20.dp)
            ){
                Column(

                ){
                    Text(
                        text = stringResource(R.string.words),
                        color = warnaUngu,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = word,
                        onValueChange = { newText ->
                            if (newText.length <= 14) {
                                onWordChange(newText)
                            }

                        },


                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ) ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp, 10.dp))
                            .padding(vertical = 10.dp),

                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = warnaUngu,
                            focusedBorderColor = warnaUngu,
                            focusedLabelColor = warnaUngu,
                            unfocusedLabelColor = warnaHitam,
                        ),
                        shape = RoundedCornerShape(15.dp),
                        trailingIcon = {
                            IconPicker(isError = wordsError, imageVector = Icons.Default.AddCircle)
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = warnaHitam
                        ),
                        supportingText = {
                            ErrorHint(wordsError)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.ex_words),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = warnaAbu
                            )
                        },

                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.meaning_2),
                        color = warnaUngu,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = meaning,
                        onValueChange = { newText ->
                            if (newText.length <= 14) {
                                onMeaningChange(newText)
                            }

                        },


                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = warnaUngu,
                            focusedBorderColor = warnaUngu,
                            focusedLabelColor = warnaUngu,
                            unfocusedLabelColor = warnaHitam,
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp, 10.dp))
                            .padding(vertical = 10.dp),
                        trailingIcon = {
                            IconPicker(isError = meaningError, imageVector = Icons.Default.Info)
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = warnaHitam
                        ),
                        supportingText = {
                            ErrorHint(meaningError)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.ex_meaning),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = warnaAbu
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.notes),
                        color = warnaUngu,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = { newText ->
                            if (newText.length <= 30) {
                                onNoteChange(newText)
                            }
                        },

                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = warnaUngu,
                            focusedBorderColor = warnaUngu,
                            focusedLabelColor = warnaUngu,
                            unfocusedLabelColor = warnaHitam,
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp, 10.dp))
                            .padding(vertical = 10.dp),
                        trailingIcon = {
                            IconPicker(isError = notesError, imageVector = Icons.Default.DateRange)
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = warnaHitam
                        ),
                        supportingText = {
                            ErrorHint(notesError)
                        },
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = stringResource(R.string.ex_meaning),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = warnaAbu
                            )
                        },
                    )
                    InputWaktu(isChecked = isChecked,
                        onCheckedChange = onCheckedChange) ?: false


                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {

                            wordsError = (word == "")
                            meaningError = (meaning == "")
                            notesError = (note == "")

                            if(wordsError || meaningError || notesError) {

                            } else {
                                if(id == null){
                                    if(isChecked){
                                        viewModel.insertWithDate(word, meaning, note)
                                    } else {
                                        viewModel.insert(word, meaning, note)
                                    }
                                } else {
                                    viewModel.update(id, word, meaning, note)
                                }
                                navController.popBackStack()
                                Toast.makeText(context, R.string.data_add, Toast.LENGTH_SHORT).show()

                            }


                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(50.dp),
                        shape = RoundedCornerShape(15.dp),
                        content = {
                            Text(
                                text = stringResource(R.string.add_words),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = warnaPutih
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = warnaUngu,
                            contentColor = warnaPutih
                        )
                    )
                }

            }
        }

    }


}

@Composable
fun IconPicker(isError: Boolean, imageVector: ImageVector){
    if(isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Icon(
            imageVector = imageVector,
            tint = Color.Gray,
            contentDescription = null,
        )
    }
}
@Composable
fun ErrorHint(isError: Boolean){
    if(isError){
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Composable
fun InputWaktu(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit): Boolean? {

    val checkedState = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = warnaUngu, uncheckedColor = warnaAbu)
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(R.string.save_date),
                color = warnaHitam,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,

                )
        }

    }
    return checkedState.value
}

@Composable
fun DeleteAction(delete: () -> Unit){
    var expanded by remember { mutableStateOf(false)  }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = stringResource(R.string.lainnya),
            tint = warnaPutih
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                       Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Wordz3Theme {
        DetailScreen(rememberNavController())
    }
}