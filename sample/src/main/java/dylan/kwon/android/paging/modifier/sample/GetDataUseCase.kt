package dylan.kwon.android.paging.modifier.sample

class GetDataUseCase {

    operator fun invoke(
        id: Int,
        count: Int = 30
    ): List<MyData>? {
        if (id > 100) {
            return null
        }
        return mutableListOf<MyData>().apply {
            val now = System.currentTimeMillis()
            repeat(count) {
                val dataId = id + it
                this += MyData(
                    id = dataId,
                    title = "From PageSource",
                    createdAt = now
                )
            }
        }
    }
}