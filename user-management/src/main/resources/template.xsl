<xsl:stylesheet version ="1.0" xmlns:xsl ="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match = "/class">
        <html>
            <body>
                <h2>Hi, <xsl:value-of select="name"/></h2>
                <h3>Verify your email</h3>
                <p>Please click the button below to verify your ownership of this email for the account.</p>
                <a href="{href}"><button>Verify your account</button></a>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>