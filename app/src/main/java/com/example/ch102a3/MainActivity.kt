package com.example.ch102a3

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import com.example.ch102a3.ui.theme.CH102A3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserProfileScreen()
                }
            }
        }



@Composable
fun UserProfileScreen() {
    var user1 by remember {
        mutableStateOf(
            value= User(
                fullName = "John Lennon",
                age = 85,
                birthday = "10/09/1948",
                address = "London, England",
                username = "@jlennon",
                isVerified = true,
                likesCount = 0

            )
        )


    }

    // CHALLENGE 3
    val friends = remember {
        mutableStateListOf<String>(
            "Greg",
            "Bob",
            "Jane",
            "Mary",
            "Jerry"
        )
    }

    // CHALLENGE 4
    var newUsername by remember{
        mutableStateOf(user1.username)
    }
    var newAge by remember {
        mutableStateOf(user1.age.toString())
    }
    var newFriendName by remember {
        mutableStateOf("")
    }


            Surface(
                modifier = Modifier.fillMaxSize(),

            ) {
                ProfileContent(
                    user = user1,
                    friends = friends,
                    newUsername = newUsername,
                    newAge = newAge,
                    newFriendName = newFriendName,

                    // ACTIONS
                    onUsernameChange = { newUsername = it },
                    onAgeChange = { newAge = it },
                    onFriendNameChange = { newFriendName = it },
                    onLike = { user1 = user1.copy(likesCount = user1.likesCount + 1) },
                    onUpdateProfile = {
                        val ageInt = newAge.toIntOrNull()
                        if(ageInt != null ) {
                            user1 = user1.updateProfile(newUsername, ageInt)

                        }
                    },
                    onAddFriend = {
                        user1.addFriend(friends, newFriendName)
                        newFriendName = ""
                    },
                    onRemoveFriend = { friendName -> user1.removeFriend(friends, friendName) },
                    onResetLikes = {
                        user1 = user1.copy(likesCount = 0) }


                )
            }

                    }




@Composable
fun ProfileContent(
    user: User,
    friends: List<String>,
    newUsername: String,
    newAge: String,
    newFriendName: String,
    onUsernameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onFriendNameChange: (String) -> Unit,
    onLike: () -> Unit,
    onUpdateProfile: () -> Unit,
    onAddFriend: () -> Unit,
    onRemoveFriend: (String) -> Unit,
    onResetLikes: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserProfileCard(user)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = newUsername,
            onValueChange = onUsernameChange,
            label = { Text("Edit Username") },
            modifier = Modifier.fillMaxWidth()

        )

        // CHALLENGE 6

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = newAge,
            onValueChange = onAgeChange,
            label = { Text("Edit Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onUpdateProfile, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Update Profile")

        }

        // CHALLENGE 7

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = newFriendName,
            onValueChange = onFriendNameChange,
            label = { Text("Enter new friend's Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // CHALLENGE 8

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onAddFriend, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add Friend")

        }

        Spacer(modifier = Modifier.height(12.dp))

        FriendList(
            friends = friends,
            onRemoveFriend = onRemoveFriend

        )
        // CHALLENGE 9
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp))
        {
            // CHALLENGE 10
            Button(onClick = onLike,modifier = Modifier.fillMaxWidth() ) {
                Text(text = "Like")
            }

        }


    }
    }

@Composable
fun UserProfileCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = user.fullName)
            Text(text = user.username)
            Spacer(modifier = Modifier.height(8.dp))
            // CHALLENGE 5
            Text(text = "Age: ${user.age}")
            Text(text = "Birthday: ${user.birthday}")
            Text(text = "Address: ${user.address}")
            Text(text = "Is Verified: ${if (user.isVerified) "Yes" else "No"}")
            Text(text = "Likes: ${user.likesCount}")
            Text(text = "Age Group: ${user.getAgeGroup(user.age)}")
        }
    }
}

@Composable
fun FriendItem(friendName: String, onRemoveFriend: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = friendName,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onRemoveFriend
            ) {
                Text(text = "Remove")
            }


        }
    }
}
@Composable
fun FriendList(friends: List<String>, onRemoveFriend: (String) -> Unit) {
    Text(
        text = "Friends (${friends.size})",
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )

    if (friends.isEmpty()) {
        Text("No friends added yet.")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(friends) { friend ->
                FriendItem(friendName = friend, onRemoveFriend = { onRemoveFriend(friend) }
                )
            }
        }

    }
}