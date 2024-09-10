package com.example.wishlist

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.wishlist.ui.theme.WishlistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WishlistTheme {
                WishlistApp()
            }
        }
    }
}

@Composable
fun WishlistApp() {
    var wishlistItems by remember { mutableStateOf(listOf<WishlistItem>()) }

    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the list of items
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Takes up remaining space
                .fillMaxWidth()
        ) {
            items(wishlistItems) { item ->
                WishlistItemRow(item)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields and button
        Column {
            TextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = itemPrice,
                onValueChange = { itemPrice = it },
                label = { Text("Item Price") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = itemUrl,
                onValueChange = { itemUrl = it },
                label = { Text("Item URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (itemName.isNotBlank() && itemPrice.isNotBlank() && itemUrl.isNotBlank()) {
                        // Convert itemPrice to Double
                        val price = itemPrice.toDoubleOrNull() ?: 0.0
                        wishlistItems = wishlistItems + WishlistItem(itemName, price, itemUrl)
                        itemName = ""
                        itemPrice = ""
                        itemUrl = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Item")
            }
        }
    }
}

@Composable
fun WishlistItemRow(item: WishlistItem) {
    val context = LocalContext.current // Get context for Toast and Intent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    ContextCompat.startActivity(context, browserIntent, null)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "Invalid URL for " + item.name, Toast.LENGTH_LONG).show()
                }
            }
    ) {
        Text(text = "Name: ${item.name}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Price: ${item.price}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "URL: ${item.url}", style = MaterialTheme.typography.bodyLarge)
        Divider(modifier = Modifier.padding(vertical = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WishlistTheme {
        WishlistApp()
    }
}


