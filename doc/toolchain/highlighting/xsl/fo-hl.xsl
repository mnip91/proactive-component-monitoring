<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:d="http://docbook.org/ns/docbook"
xmlns:fo="http://www.w3.org/1999/XSL/Format"
		xmlns:xslthl="http://xslthl.sf.net"
                exclude-result-prefixes="xslthl d"
                version='1.0'>

<!-- ********************************************************************
     $Id: highlight.xsl 7266 2007-08-22 11:58:42Z xmldoc $
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://docbook.sf.net/release/xsl/current/ for
     and other information.

     ******************************************************************** -->

<xsl:template match='xslthl:keyword'>
  <fo:inline font-weight="bold" color="#0101ff"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:string'>
  <fo:inline font-weight="bold" color="#ff2aff"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:comment'>
  <fo:inline font-style="italic" color="#018101"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:tag'>
  <fo:inline font-weight="bold" color="#0101ff"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:attribute'>
  <fo:inline font-weight="bold" color="#ff0101"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:value'>
  <fo:inline font-weight="bold" color="#ff2aff"><xsl:apply-templates/></fo:inline>
</xsl:template>

<xsl:template match='xslthl:html'>
  <span style='background:#AFF'><font color='blue'><xsl:apply-templates/></font></span>
</xsl:template>

<xsl:template match='xslthl:xslt'>
  <span style='background:#AAA'><font color='blue'><xsl:apply-templates/></font></span>
</xsl:template>

<xsl:template match='xslthl:section'>
  <span style='background:yellow'><xsl:apply-templates/></span>
</xsl:template>

</xsl:stylesheet>
