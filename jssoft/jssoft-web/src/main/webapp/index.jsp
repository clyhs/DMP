<%@ page contentType="text/html; charset=gbk"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
<head>
<title>�û�ע��</title>
</head>
<br>
<f:view>
	<h:form id="helloForm">
		<table border="10" align="center" bordercolor="#0099CC"
			cellpadding="6" bordercolorlight="#999999">
			<tr>
				<td colspan="2" bgcolor="#66CCFF">�����û�ע����Ϣ��</td>
			</tr>
			<tr>
				<td>
					<div align="right">�û���</div>
				</td>
				<td><h:inputText id="username" value="">

					</h:inputText></td>
			</tr>
			<tr>
				<td>
					<div align="right">E_mail</div>
				</td>
				<td><h:inputText id="email" value="" /></td>
			</tr>
			<tr>
				<td colspan="2" bgcolor="#FFFF40"><span> <h:message
							id="message" for="username" /></span></td>
			</tr>
			<tr>
				<td align="center" colspan="2"><h:commandButton id="submit"
						action="" value="�ύ" /></td>
			</tr>
		</table>
	</h:form>
</f:view>
</html>
