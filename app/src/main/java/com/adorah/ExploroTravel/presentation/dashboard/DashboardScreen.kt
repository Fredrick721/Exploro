package com.adorah.ExploroTravel.presentation.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.adorah.ExploroTravel.models.ExploroItem
import com.example.todo.presentation.screens.addtodo.AddExploroForm
import com.example.todo.presentation.screens.addtodo.EditExploroForm

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// THIS FILE WILL CONTAIN THE COMPOSABLE ELEMENTS TO DISPLAT MY LIST OF TODos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController){

    // to create a list of composables {listview}
    // add a dialog
    val showAddDialog = remember { mutableStateOf(false) }
    // show edit dialog
    val showEditDialog = remember {mutableStateOf(false)}
    // selected to do
    val ExploroBeingEdited = remember { mutableStateOf< ExploroItem?>(
        null
    ) }
    // drawer state reference
    val drawerState = rememberDrawerState(initialValue =
    DrawerValue.Closed)
    // coroutine scope : handle configs on device change
    val corountineScope = rememberCoroutineScope()
   ModalNavigationDrawer(
       drawerContent = {
           DrawerContent(
               onNavigateToHome = {
                   navController.navigate("dashboard")
               },
               onLogout = {
                   FirebaseAuth.getInstance().signOut()
                   navController.navigate("login"){
                       popUpTo("dashboard")
                       {inclusive = true}
                   }
               },

           )
       } ,
       drawerState = drawerState
   ) {
       Scaffold(
           topBar = {
               TopAppBar(
                   title =  { Text("Dashboard") },
                   navigationIcon = {
                         IconButton(
                             onClick = {
                                 corountineScope.launch {
                                     drawerState.open()
                                 }
                             }
                         ) {
                             Icon(Icons.Default.Menu,
                                 contentDescription = "Menu")
                         }
                   }
               )
           },
           floatingActionButton = {
               FloatingActionButton(
                   onClick = {showAddDialog.value = true}
               ) {
                   Icon(Icons.Default.Add,
                       contentDescription = "Add Exploro")
               }
           }
       ) { padding ->

           // button -> redirect to the screen with the list from
           // the api
           Button(onClick =
           {  navController.navigate("")    }) {
               Text("Go to List Screen")
           }
           LazyColumn(modifier = Modifier.padding(padding)) {

           }
           // SHOW POP UP IF ALERT DIALOG IS TRUE
           if(showAddDialog.value){
               // show pop up
               // AlertDialog is used to show pop ups
               AlertDialog(
                   onDismissRequest = { showAddDialog.value = false},
                   title = { Text("Add Exploro") },
                   text = {
                       AddExploroForm(
                           onDismiss = {showAddDialog.value = false}
                       )
                   },
                   confirmButton = {},
                   dismissButton = {}
               )
           }
           // EDIT DIALOG
           if(showEditDialog.value &&
               ExploroBeingEdited.value != null){
               // show pop up
               AlertDialog(
                   onDismissRequest = { showEditDialog.value = false},
                   title = { Text("Edit Exploro") },
                   text = {
                       EditExploroForm(
                           exploro = ExploroBeingEdited.value!!,
                           onSubmit = {updatedExpoloro ->

                           },
                           onDismiss = {showEditDialog.value= false}
                       )
                   },
                   confirmButton = {}
               )
           }

       }
   }
   }

@Composable
fun DrawerContent(onNavigateToHome: () -> Unit, onLogout: () -> Unit) {
    TODO("Not yet implemented")
}











