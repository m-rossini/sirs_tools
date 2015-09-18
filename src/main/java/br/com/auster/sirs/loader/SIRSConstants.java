/*
 * Copyright (c) 2004-2005 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Apr 1, 2005
 */
package br.com.auster.sirs.loader;

/**
 * <p><b>Title:</b> SIRSConstants</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004-2005</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: SIRSConstants.java 154 2007-05-27 23:50:16Z mtengelm $
 */

public class SIRSConstants {
  private SIRSConstants() {}
  
  public static final String CALL_CLASS_NC          = "N/C";
  public static final String CALL_CLASS_VC1H        = "VC1H";
  public static final String CALL_CLASS_FRANQUIA    = "FRANQUIA";
  public static final String CALL_CLASS_ATEND       = "*1234/*555";
  public static final String CALL_CLASS_VC1SPV      = "VC1SPV";
  public static final String CALL_CLASS_VC1C        = "VC1C";
  public static final String CALL_CLASS_ROI         = "ROI";
  public static final String CALL_CLASS_MODEM       = "MODEM";
  public static final String CALL_CLASS_WAP         = "WAP";
  public static final String CALL_CLASS_ASSIST      = "MULTIASSISTÊNCIA";
  public static final String CALL_CLASS_VOX         = "CLICKFONE VOX";
  public static final String CALL_CLASS_SPECIAL     = "NÚMEROS ESPECIAIS";
  public static final String CALL_CLASS_ADICIONAL   = "ADICIONAL";
  public static final String CALL_CLASS_AGGREGATE   = "VIRTUAL CALL";
  
  public static final String CALL_SUBCLASS_NC       = "N/C";
  public static final String CALL_SUBCLASS_MM       = "MM";
  public static final String CALL_SUBCLASS_MF       = "MF";
  public static final String CALL_SUBCLASS_MMI      = "MMI";
  public static final String CALL_SUBCLASS_GRUPO    = "GRUPO";
  public static final String CALL_SUBCLASS_VC1      = "VC1";
  public static final String CALL_SUBCLASS_555      = "*555";
  public static final String CALL_SUBCLASS_WAAAP    = "WAAAP";
  public static final String CALL_SUBCLASS_VC1SPV   = "VC1SPV";
  public static final String CALL_SUBCLASS_EUROPA   = "EUROPA";
  public static final String CALL_SUBCLASS_AMERICA  = "AMÉRICA";
  public static final String CALL_SUBCLASS_MERCOSUL = "MERCOSUL";
  public static final String CALL_SUBCLASS_7000     = "*7000/*365";
  public static final String CALL_SUBCLASS_3000     = "*3000";
  public static final String CALL_SUBCLASS_ORIGINADO = "ORIGINADO";
  public static final String CALL_SUBCLASS_RECEBIDO = "RECEBIDO";
  
  public static final String CALL_TARIFF_PEAK     = "P";
  public static final String CALL_TARIFF_OFFPEAK  = "O";
  public static final String CALL_TARIFF_UNIQUE   = "U";
  public static final String CALL_TARIFF_EVENT    = "E";
}
