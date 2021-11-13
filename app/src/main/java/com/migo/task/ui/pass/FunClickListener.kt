package com.migo.task.ui.pass

import com.migo.task.model.vo.Pass

class FunClickListener(
    /**
     * Item click event
     */
    val onItemClick: (Pass) -> Unit = { },
    /**
     * Item activate click event
     */
    val onActivateClick: (Pass) -> Unit = { },
)