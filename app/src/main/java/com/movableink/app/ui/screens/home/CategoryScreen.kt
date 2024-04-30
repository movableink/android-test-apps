package com.movableink.app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movableink.app.R

@Composable
fun CategoryScreen(
    onCategoryClick: (String, String) -> Unit,
    onBackClicked: () -> Unit,
    homeViewModel: HomeViewModel,
) {
    val homeUIState by homeViewModel.homeUIState.collectAsStateWithLifecycle()
    val selectedGender = remember {
        homeUIState.gender
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),

    ) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.statusBars.add(WindowInsets(top = 60.dp)),
            ),
        )
        CategoryList(onCategoryClick, homeUIState.categories, homeUIState.gender, homeViewModel)
    }
    CategoryBar(gender = selectedGender, onBackClicked = onBackClicked)
}

@Composable
fun CategoryList(
    onCategoryClick: (String, String) -> Unit,
    categories: List<String>,
    gender: String,
    homeViewModel: HomeViewModel,
) {
    LazyColumn {
        item {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 28.dp)),
                ),
            )
        }

        items(categories) { category ->
            CategoryRow(
                category = category,
                onCategoryClick = onCategoryClick,
                gender,
                homeViewModel,
            )
        }
    }
}

@Composable
fun CategoryRow(
    category: String,
    onCategoryClick: (category: String, gender: String) -> Unit,
    gender: String,
    homeViewModel: HomeViewModel,
) {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable {
                homeViewModel.updateCatalogByCategory(category, gender)
                onCategoryClick(category, gender)
            },
    ) {
        Row(modifier = Modifier.padding(all = 10.dp)) {
            Text(
                text = category,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Composable
fun CategoryBar(modifier: Modifier = Modifier, gender: String?, onBackClicked: () -> Unit) {
    Column(modifier = modifier.statusBarsPadding()) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = Color.Black,
            elevation = 0.dp,

        ) {
            IconButton(
                onClick = onBackClicked,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .size(24.dp, 24.dp)
                    .align(Alignment.CenterVertically),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.app_name),
                )
            }
            Text(
                text = "$gender",
                style = MaterialTheme.typography.h4,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterVertically),
            )
        }
    }
}
