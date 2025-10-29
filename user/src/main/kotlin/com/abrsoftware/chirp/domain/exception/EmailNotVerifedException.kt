package com.abrsoftware.chirp.domain.exception

import java.lang.RuntimeException

class EmailNotVerifedException: RuntimeException(
    "Email not verifed",
) {
}