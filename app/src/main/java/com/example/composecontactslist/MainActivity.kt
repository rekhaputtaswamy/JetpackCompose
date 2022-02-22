package com.example.composecontactslist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.composecontactslist.ui.MyTheme
import com.example.composecontactslist.ui.lightGreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                ContactsScreen()
            }
        }
    }

    @Composable
    fun ContactsScreen(contacts: List<Contacts> = contactsList) {
        Scaffold(topBar = { AppBar() }) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn {
                    items(contacts) { contact ->
                        ContactCard(contact = contact)
                    }
                }
            }
        }
    }

    @Composable
    fun AppBar() {
        TopAppBar(
            navigationIcon = {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "",
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            },
            title = { Text("My Contacts") }
        )
    }

    @Composable
    fun ContactCard(contact: Contacts) {
        Card(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Top),
            elevation = 8.dp,
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ContactPicture(contact.imageUrl, contact.status)
                ContactContent(contact.name, contact.status)
            }

        }
    }

    @Composable
    fun ContactPicture(imageUrl: String, onlineStatus: Boolean) {
        Card(
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = if (onlineStatus)
                    MaterialTheme.colors.lightGreen
                else Color.Red
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(72.dp),
            elevation = 4.dp
        ) {
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    },
                ),
                modifier = Modifier.size(72.dp),
                contentDescription = "Contacts picture",
            )
        }
    }

    @Composable
    fun ContactContent(contactName: String, onlineStatus: Boolean) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
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
    fun DefaultPreview() {
        MyTheme {
            ContactsScreen()
        }
    }

}