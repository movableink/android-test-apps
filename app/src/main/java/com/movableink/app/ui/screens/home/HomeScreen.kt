package com.movableink.app.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.movableink.app.R
import com.movableink.app.ui.component.MovableTopBar

@Composable
fun HomeScreen(
    onGenderSelected: (String) -> Unit,
    homeViewModel: HomeViewModel,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box {
            GenderList(onGenderSelected, homeViewModel)
            MovableTopBar(title = stringResource(id = R.string.app_name))
        }
    }
}

@Composable
fun GenderList(onGenderClick: (String) -> Unit, homeViewModel: HomeViewModel) {
    val genderList = remember { listOf("Men", "Women") }
    LazyColumn {
        item {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 60.dp)),
                ),
            )
        }

        items(genderList) { gender ->
            GenderRow(
                gender = gender,
                onGenderClick = onGenderClick,
                homeViewModel,
            )
        }
    }
}

@Composable
fun GenderRow(gender: String, onGenderClick: (gender: String) -> Unit, homeViewModel: HomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier.clickable {
                homeViewModel.updateCategories(gender)
                onGenderClick(gender)
            },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
            ) {
                Text(
                    text = gender,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp),
                )
                Column(horizontalAlignment = Alignment.End) {
                    Image(
                        modifier = Modifier
                            .size(24.dp, 24.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                        alignment = Alignment.Center,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    MovableTopBar(title = "")
}
