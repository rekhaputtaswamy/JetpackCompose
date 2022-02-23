package com.example.composecontactslist

import android.os.Bundle
import android.service.autofill.OnClickAction
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.composecontactslist.ui.MyTheme
import com.example.composecontactslist.ui.lightGreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                ContactsApplication()
            }
        }
    }

    @Composable
    fun ContactsApplication(contacts: List<Contacts> = contactsList) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "contacts_list") {
            composable("contacts_list") {
                ContactsListScreen(contacts, navController)
            }
            composable(route = "contact_details/{id}",
                       arguments = listOf(navArgument("id") {
                           type = NavType.IntType
                       })
            ) {
                navBackStackEntry ->
                    ContactDetailsScreen(navBackStackEntry.arguments!!.getInt("id"), navController)
            }
        }
    }

    @Composable
    fun ContactsListScreen(contacts: List<Contacts> = contactsList, navController: NavHostController?) {
        Scaffold(topBar = {
            AppBar(
                title = "Users list",
                icon = Icons.Default.AccountCircle
            ) { }
        }) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn {
                    items(contacts) { contact ->
                        ContactCard(contact = contact) {
                            navController?.navigate("contact_details/${contact.id}")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ContactDetailsScreen(id: Int, navController: NavHostController?) {
        val contactDetail = contactsList.first {contact -> id == contact.id }
        Scaffold(topBar = {
            AppBar(
                title = "Contact Details",
                icon = Icons.Default.ArrowBack
            ) {
                navController?.navigateUp()
            }
        }) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(modifier = Modifier.fillMaxWidth(),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.Top) {
                    ContactPicture(contactDetail.imageUrl, contactDetail.status, 220.dp)
                    ContactContent(contactName = contactDetail.name, onlineStatus = contactDetail.status, Alignment.CenterHorizontally)
                }
            }
        }
    }

    @Composable
    fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit) {
        TopAppBar(
            navigationIcon = {
                Icon(
                    icon,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable(onClick = { iconClickAction.invoke() })
                )
            },
            title = { Text(title) }
        )
    }

    @Composable
    fun ContactCard(contact: Contacts, clickAction: () -> Unit) {
        Card(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top)
                .clickable(onClick = { clickAction.invoke() }),
            elevation = 8.dp,
            backgroundColor = Color.White
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ContactPicture(imageUrl = contact.imageUrl, status = contact.status, imageSize = 72.dp)
                ContactContent(
                    contactName = contact.name,
                    onlineStatus = contact.status,
                    alignment = Alignment.Start)
            }
        }
    }

    @Composable
    fun ContactPicture(imageUrl: String, status: Boolean, imageSize: Dp) {
        Card(
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = if(status)
                     MaterialTheme.colors.lightGreen
                else Color.Red
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(imageSize),
            elevation = 4.dp
        ){
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    },
                ),
                modifier = Modifier.size(72.dp),
                contentDescription = "Contact Icon description"
            )
        }
    }

    @Composable
    fun ContactContent(contactName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
        Column(
            modifier = Modifier
                .padding(8.dp),
                horizontalAlignment = alignment
        ) {
            CompositionLocalProvider(
                LocalContentAlpha provides (
                        if (onlineStatus)
                            1f else ContentAlpha.medium)
            ) {
                Text(
                    text = contactName,
                    style = MaterialTheme.typography.h6
                )
            }
            CompositionLocalProvider(LocalContentAlpha provides (ContentAlpha.medium)) {
                Text(
                    text = if (onlineStatus)
                        "Online"
                    else "Offline",
                    style = MaterialTheme.typography.body2
                )
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun ContactDetailsPreview() {
        MyTheme {
            ContactDetailsScreen(id = 0, null)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ContactsListPreview() {
        MyTheme {
            ContactsListScreen(contacts = contactsList, null)
        }
    }

}