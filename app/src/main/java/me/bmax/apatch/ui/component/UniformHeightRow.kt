package me.bmax.apatch.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A row-like layout that distributes children equally in width and ensures
 * all children have the same height (matching the tallest child).
 *
 * Uses [SubcomposeLayout] to perform a two-pass approach where each child
 * is only measured once, avoiding crashes with SubcomposeLayout-based
 * children (AnimatedVisibility, LazyRow, etc.).
 *
 * Pass 1: Subcompose & measure children with unlimited height to find max.
 * Pass 2: Subcompose & measure children again with the uniform max height.
 * Because SubcomposeLayout creates new composition slots each pass, each
 * child is only ever measured once per subcomposition.
 */
@Composable
fun UniformHeightRow(
    modifier: Modifier = Modifier,
    spacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val spacingPx = spacing.roundToPx()

        // First pass: measure with unlimited height to find the tallest child
        val firstPassPlaceables = subcompose("first", content).map { measurable ->
            measurable.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = constraints.maxWidth,
                    minHeight = 0,
                    maxHeight = Constraints.Infinity
                )
            )
        }

        if (firstPassPlaceables.isEmpty()) {
            return@SubcomposeLayout layout(constraints.minWidth, constraints.minHeight) {}
        }

        val maxChildHeight = firstPassPlaceables.maxOf { it.height }
        val count = firstPassPlaceables.size
        val totalSpacing = spacingPx * (count - 1).coerceAtLeast(0)
        val availableForChildren = (constraints.maxWidth - totalSpacing).coerceAtLeast(0)
        val childWidth = availableForChildren / count

        // Second pass: re-subcompose with uniform height constraints
        val placeables = subcompose("second", content).map { measurable ->
            measurable.measure(
                Constraints(
                    minWidth = childWidth,
                    maxWidth = childWidth,
                    minHeight = maxChildHeight,
                    maxHeight = maxChildHeight
                )
            )
        }

        layout(constraints.maxWidth, maxChildHeight) {
            placeables.forEachIndexed { index, placeable ->
                val x = index * (childWidth + spacingPx)
                placeable.placeRelative(x, 0)
            }
        }
    }
}
