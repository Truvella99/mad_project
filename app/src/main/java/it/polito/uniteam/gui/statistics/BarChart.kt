package it.polito.uniteam.gui.statistics

import android.annotation.SuppressLint
import android.text.Layout
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shape.markerCornered
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.cartesian.DefaultPointConnector
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.Insets
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.copyColor
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Corner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.random.Random
import com.patrykandpatrick.vico.core.common.shape.Shape
import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.rememberLegendItem
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import it.polito.uniteam.Factory
import kotlinx.coroutines.withContext

@Composable
fun BarChart(vm: StatisticsViewModel = viewModel(factory = Factory(LocalContext.current))) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val data = mapOf(
        "member1" to Pair(6f, 8f),
        "member2" to Pair(6f, 4f),
        "member3" to Pair(5f, 6f),
        "member4" to Pair(7f, 3f),
        "member5" to Pair(4f, 9f),
        "member6" to Pair(8f, 2f),
        "member7" to Pair(7f, 3f),
        "member8" to Pair(4f, 9f),
        "member9" to Pair(8f, 2f),
        "member10" to Pair(5f, 7f),
        "member11" to Pair(3f, 8f),
        "member12" to Pair(6f, 5f),
        "member13" to Pair(9f, 4f),
        "member14" to Pair(2f, 6f),
        "member15" to Pair(7f, 5f),
        "member16" to Pair(3f, 7f),
        "member17" to Pair(8f, 6f),
        "member18" to Pair(4f, 8f)
    )
    val labelListKey = ExtraStore.Key<List<String>>()
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            modelProducer.tryRunTransaction {
                columnSeries {
                    series(
                        data.values.map {
                            it.first
                        }
                    )
                    series(
                        data.values.map {
                            it.second
                        }
                    )
                }
                updateExtras {
                    it[labelListKey] = data.keys.toList()
                }
                //lineSeries { series(List(Defaults.ENTRY_COUNT) { Random.nextFloat() * Defaults.MAX_Y }) }
            }
        }
    }
    val maxY = 11
    val lineColor = Color.White
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider =
                ColumnCartesianLayer.ColumnProvider.series(
                    chartColors.map { color ->
                        rememberLineComponent(
                            color = color,
                            thickness = 20.dp,
                            shape = Shape.rounded(2.dp),
                        )
                    },
                )
            ),
            rememberLineCartesianLayer(
                lines =
                listOf(
                    rememberLineSpec(
                        shader = DynamicShader.color(lineColor),
                        pointConnector = DefaultPointConnector(cubicStrength = 0f),
                    ),
                ),
            ),
            startAxis = rememberStartAxis(itemPlacer = remember { AxisItemPlacer.Vertical.step({ _ -> Math.ceil((maxY / 10).toDouble()).toFloat() }) }),
            bottomAxis = rememberBottomAxis(valueFormatter = CartesianValueFormatter { x, chartValues, _ -> chartValues.model.extraStore[labelListKey][x.toInt()] }),
            legend = rememberLegend()
        ),
        modelProducer = modelProducer,
        modifier = Modifier.fillMaxHeight(0.9f),
        marker = rememberMarker(),
        runInitialAnimation = true,
        zoomState = rememberVicoZoomState(zoomEnabled = false)
    )
}

@Composable
private fun rememberLegend() =
    rememberHorizontalLegend<CartesianMeasureContext, CartesianDrawContext>(
        items =
        chartColors.mapIndexed { index, chartColor ->
            rememberLegendItem(
                icon = rememberShapeComponent(Shape.Pill, chartColor),
                label =
                rememberTextComponent(
                    color = vicoTheme.textColor,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE,
                ),
                labelText = if(index == 0) "Planned" else "Spent",
            )
        },
        iconSize = 8.dp,
        iconPadding = 8.dp,
        spacing = 8.dp,
        padding = Dimensions.of(top = 8.dp),
    )

private val chartColors = listOf(Color(0xff916cda), Color(0xffd877d8))

@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = Shape.markerCornered(Corner.FullyRounded)
    val labelBackground =
        rememberShapeComponent(labelBackgroundShape, MaterialTheme.colorScheme.surface)
            .setShadow(
                radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP,
                dy = LABEL_BACKGROUND_SHADOW_DY_DP,
                applyElevationOverlay = true,
            )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            background = labelBackground,
            padding = Dimensions.of(8.dp, 4.dp),
            typeface = Typeface.MONOSPACE,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent = rememberShapeComponent(Shape.Pill, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = rememberShapeComponent(Shape.Pill)
    val indicatorRearComponent = rememberShapeComponent(Shape.Pill)
    val indicator =
        rememberLayeredComponent(
            rear = indicatorRearComponent,
            front =
            rememberLayeredComponent(
                rear = indicatorCenterComponent,
                front = indicatorFrontComponent,
                padding = Dimensions.of(5.dp),
            ),
            padding = Dimensions.of(10.dp),
        )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        @SuppressLint("RestrictedApi")
        object : DefaultCartesianMarker(
            label = label,
            labelPosition = labelPosition,
            indicator = if (showIndicator) indicator else null,
            indicatorSizeDp = 36f,
            setIndicatorColor =
            if (showIndicator) {
                { color ->
                    indicatorRearComponent.color = color.copyColor(alpha = .15f)
                    indicatorCenterComponent.color = color
                    indicatorCenterComponent.setShadow(radius = 12f, color = color)
                }
            } else {
                null
            },
            guideline = guideline,
        ) {
            override fun getInsets(
                context: CartesianMeasureContext,
                outInsets: Insets,
                horizontalDimensions: HorizontalDimensions,
            ) {
                with(context) {
                    outInsets.top =
                        (
                                CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP -
                                        LABEL_BACKGROUND_SHADOW_DY_DP
                                )
                            .pixels
                    if (labelPosition == LabelPosition.AroundPoint) return
                    outInsets.top += label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels
                }
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f
