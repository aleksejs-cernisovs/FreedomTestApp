package aleksejs.cernisovs.freedomTestApp.feature.shared.ui.annotation

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Extra Small",
    group = "Different Device Sizes",
    device = "spec:width=320dp,height=568dp"
)
@Preview(
    name = "Small",
    group = "Different Device Sizes",
    device = "spec:width=360dp,height=640dp"
)
@Preview(
    name = "Medium",
    group = "Different Device Sizes",
    device = "spec:width=393dp,height=852dp"
)
@Preview(
    name = "Large",
    group = "Different Device Sizes",
    device = "spec:width=432dp,height=960dp"
)
@Preview(
    name = "Extra Large",
    group = "Different Device Sizes",
    device = "spec:width=600dp,height=960dp"
)
@Preview(
    name = "Tablet",
    group = "Different Device Sizes",
    device = "spec:width=800dp,height=1280dp"
)
@Preview(
    name = "Tablet Horizontal",
    group = "Different Device Sizes",
    device = "spec:width=1280dp,height=800dp"
)
annotation class PreviewDifferentDeviceSizes