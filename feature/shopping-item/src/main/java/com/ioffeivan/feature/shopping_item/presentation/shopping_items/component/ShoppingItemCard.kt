package com.ioffeivan.feature.shopping_item.presentation.shopping_items.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.model.ShoppingItem
import com.ioffeivan.feature.shopping_item.R

@Composable
fun ShoppingItemCard(
    shoppingItem: ShoppingItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text = shoppingItem.name,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(R.string.quantity, shoppingItem.quantity),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun ShoppingItemCardPreviewLight() {
    PreviewContainer {
        ShoppingItemCard(
            shoppingItem = ShoppingItem(
                id = 1,
                name = "Milk",
                quantity = "2",
                listId = 1,
            )
        )
    }
}

@Preview
@Composable
fun ShoppingItemCardPreviewDark() {
    PreviewContainer(darkTheme = true) {
        ShoppingItemCard(
            shoppingItem = ShoppingItem(
                id = 1,
                name = "Milk",
                quantity = "2",
                listId = 1,
            )
        )
    }
}