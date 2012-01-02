<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/">

    <xsl:template match="wsdl:part[@name='Impersonation']">
        <xsl:comment>FIX: Removed Impersonation part</xsl:comment>
    </xsl:template>

    <xsl:template match="wsdl:part[@name='MailboxCulture']">
        <xsl:comment>FIX: Removed MailboxCulture part</xsl:comment>
    </xsl:template>

    <xsl:template match="wsdl:part[@name='RequestVersion']">
        <xsl:comment>FIX: Removed RequestVersion part</xsl:comment>
    </xsl:template>

    <xsl:template match="wsdl:part[@name='TimeZoneContext']">
        <xsl:comment>FIX: Removed TimeZoneContext part</xsl:comment>
    </xsl:template>

    <xsl:template match="soap:header[@part='Impersonation']">
        <xsl:comment>FIX: Removed Impersonation header</xsl:comment>
    </xsl:template>

    <xsl:template match="soap:header[@part='MailboxCulture']">
        <xsl:comment>FIX: Removed MailboxCulture header</xsl:comment>
    </xsl:template>

    <xsl:template match="soap:header[@part='RequestVersion']">
        <xsl:comment>FIX: Removed RequestVersion header</xsl:comment>
    </xsl:template>

    <xsl:template match="soap:header[@part='TimeZoneContext']">
        <xsl:comment>FIX: Removed TimeZoneContext header</xsl:comment>
    </xsl:template>

    <xsl:template match="wsdl:binding">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
            
        <xsl:comment>FIX: Add missing service element</xsl:comment>   
        <wsdl:service name="ExchangeWebService">
            <wsdl:port name="ExchangeWebPort" binding="tns:ExchangeServiceBinding">
                <soap:address location="https://example.com/EWS/exchange.asmx"></soap:address>
            </wsdl:port>
        </wsdl:service>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


</xsl:stylesheet>
