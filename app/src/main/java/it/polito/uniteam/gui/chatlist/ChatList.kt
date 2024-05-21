package it.polito.uniteam.gui.chatlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import it.polito.uniteam.Factory
import it.polito.uniteam.gui.chat.ChatViewModel

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.extractor.MpegAudioUtil.Header

import it.polito.uniteam.classes.Member
import it.polito.uniteam.classes.MemberIcon
import it.polito.uniteam.classes.messageStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ChatListScreen( vm : ChatListViewModel = viewModel(factory = Factory(LocalContext.current))) {
    Column(modifier = Modifier.fillMaxSize()) {
        ListHeader(vm = vm)
        UserList(vm = vm)
    }
}

@Composable
fun ListHeader(vm : ChatListViewModel ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "${vm.getTeam(1).name} Members",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}
@Composable
fun UserList(vm : ChatListViewModel) {
    //TODO: implement sorting
    //order by last message
    /*val members = vm.getMembers().sortedByDescending { member ->
        //member.messages.maxOfOrNull { it.creationDate } ?: LocalDateTime.MIN
        vm.messages.filter { it.senderId == member.id }.maxOfOrNull { it.creationDate } ?: LocalDateTime.MIN
    }*/

   /* Text( // PER CONTROLLARE ORDINE
        text = "Members: ${vm.getMembers().joinToString(", ") { it.id.toString() }}",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White
    )*/
    LazyColumn {
        items( vm.getMembers().filter { member -> member != vm.loggedMember  }) { user ->
            UserItem(member = user,vm = vm)
        }
    }
}
@Composable
fun UserItem(member: Member,vm : ChatListViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)// LEVA SE VUOI UNIRE LE RIGHE
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { /*TODO*/ }
            .padding(16.dp)
            //.height((LocalConfiguration.current.screenHeightDp * 0.2).dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberIcon(modifierScale= Modifier.scale(1f), modifierPadding = Modifier.padding(4.dp, 0.dp, 15.dp, 0.dp),member = member )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(text = member.fullName, style = MaterialTheme.typography.bodyLarge)
            //ROLE IN DUMMY DATA DA CAMBIARE TODO
            Text(text = member.teamsInfo?.get(1)?.role.toString(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
        Column (modifier = Modifier.weight(0.1f)){
            val recentMessageDate = vm.messages
                .filter { it.senderId == member.id }
                .maxOfOrNull { it.creationDate }

            recentMessageDate?.let {
                Text(
                    textAlign = TextAlign.Center,
                    text = formatMessageDate(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer// Color.Gray
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .size(50.dp)
                .clickable { /*TODO onChatClick()*/ }
        ) {
            Icon(
                imageVector = Icons.Filled.Chat,
                contentDescription = "Chat",
                modifier = Modifier
                    .fillMaxSize()
            )
            val unreadCount = vm.getUnreadMessagesCount(member.id)//vm.chat.messages.filter { it.status == messageStatus.UNREAD && it.senderId == member.id}.size
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(18.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = unreadCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }


    }
}
fun formatMessageDate(dateTime: LocalDateTime): String {
    val today = LocalDate.now()
    val messageDate = dateTime.toLocalDate()

    return when {
        ChronoUnit.DAYS.between(messageDate, today) == 0L -> "Today"
        ChronoUnit.DAYS.between(messageDate, today) == 1L -> "Yesterday"
        //ChronoUnit.MINUTES.between(messageDate, today) == 1L -> "Now"
        else -> dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}