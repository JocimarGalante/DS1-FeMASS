package com.femass.ds1.requerimentosfemass.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.omnifaces.util.Messages;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;

public class ArquivosUtil {

	/**
	 * Lista o que contem no diretorio
	 * 
	 * @param strdir - informar o caminho do diretorio.
	 * @param list   - informar a lista para preencher e mostrar em tela.
	 */
	public static void liArqDir(String strdir, List<String> list) {
		File dir = new File(strdir);
		if (dir.isDirectory()) {
			String[] arqs = dir.list();
			for (String nome : arqs) {
				System.out.println(nome);
				list.add(nome);
				System.out.println(list);
			}
		}
	}

	/**
	 * Merge multiplos pdf para um pdf ex: OutputStream out = new
	 * FileOutputStream(new File(Constantes.PATH_ARQ + "/result.pdf"));
	 * 
	 * @param list         - informar lista de pdf input stream
	 * @param outputStream - informar outputstream
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void doMerge(List<InputStream> list, OutputStream outputStream)
			throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		PdfContentByte cb = writer.getDirectContent();

		for (InputStream in : list) {
			PdfReader reader = new PdfReader(in);
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				document.newPage();
				// import the page from source pdf
				PdfImportedPage page = writer.getImportedPage(reader, i);
				// add the page to the destination pdf
				cb.addTemplate(page, 0, 0);
			}
		}
		outputStream.flush();
		document.close();
		outputStream.close();
	}

	/**
	 * PDF para Byte - recebe um path do pdf e devolve em byte
	 * 
	 * @param filePath   - caminho do arquivo
	 * @return finalData - byte do arquivo convertido
	 * @throws JRException
	 */
	public static byte[] pdfToByte(String filePath) throws JRException {

		File file = new File(filePath);
		FileInputStream fileInputStream;
		byte[] data = null;
		byte[] finalData = null;
		ByteArrayOutputStream byteArrayOutputStream = null;

		try {
			fileInputStream = new FileInputStream(file);
			data = new byte[(int) file.length()];
			finalData = new byte[(int) file.length()];
			byteArrayOutputStream = new ByteArrayOutputStream();

			fileInputStream.read(data);
			byteArrayOutputStream.write(data);
			finalData = byteArrayOutputStream.toByteArray();

			fileInputStream.close();

		} catch (FileNotFoundException e) {
			Messages.addGlobalError("Arquivo não encontrado." + e);
		} catch (IOException e) {
			Messages.addGlobalError("IO exception" + e);
		}
		return finalData;
	}

	/**
	 * Transforma byte para PDF
	 * 
	 * @param b       - Informar o byte[]
	 * @param caminho - Informar o caminho path.
	 */
	public static void byteToPdf(byte[] b, String caminho) {

		try {
			OutputStream out = new FileOutputStream(caminho + "/byteToPdf.pdf");
			out.write(b);
			out.close();
			System.out.println("byte transformado em PDF com sucesso.");
		} catch (Exception e) {
			System.out.println("Erro ao transformar byte em pdf." + e);
		}
	}

	/**
	 * Metodo para mover arquivos ou diretorios 
	 * OBS: pode mudar o nome sem problemas para o destino
	 * 
	 * Exemplo: 
	 * String caminhoOrigem = Constantes.PATH_ASSINATURA + "temp/tempMen1.png";
	 * String caminhoDestino = Constantes.PATH_ASSINATURA + "Men1.png";
	 * 
	 * @param caminhoOrigem
	 * @param caminhoDestino
	 * @throws IOException
	 */
	public static void move(String caminhoOrigem, String caminhoDestino) throws IOException {
		Path origem = Paths.get(caminhoOrigem);
		Path destino = Paths.get(caminhoDestino);

		Path sucesso = Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
		if (sucesso != null) {
			System.out.println("Arquivo Movido com sucesso.");
		}
	}
	
	/**
	 * Metodo que copiara arquivos do local temporario para a pasta correta do
	 * sistema.
	 * 
	 * @param caminhoTemp    - Constantes.PATH_IMAGEM + "temp/temp.png"
	 * @param caminhoArquivo - Constantes.PATH_IMAGEM + proCadastro.getCodigo() + ".png"
	 */
	public static void copiaFiles(String caminhoTemp, String caminhoArquivo) {
		Path origem = Paths.get(caminhoTemp);
		Path destino = Paths.get(caminhoArquivo);

		try {
			Path p = Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
			Messages.addGlobalInfo("Imagem '" + p + "' copiada com sucesso.");
			
		} catch (NoSuchFileException e) {
			Messages.addGlobalError("Origem/ Destino não existe" + e);
		} catch (FileAlreadyExistsException e) {
			Messages.addGlobalError("Arquivo já existe falha ao mover." + e);
		} catch (DirectoryNotEmptyException e) {
			Messages.addGlobalError("Destino não está vazio falha ao mover." + e);
		} catch (IOException e) {
			Messages.addGlobalError("Ocorreu um erro ao copiar o arquivo para pasta correta." + e);
		}
	}
	
	/**
	 * Deleta arquivo ou diretório
	 * 
	 * @param caminho  - Informar caminho completo inclusive a extensão em caso de arquivos
	 * @throws Exception
	 */
	public static void delFiles(String caminho) throws Exception {
	    Path p = Paths.get(caminho);

	    try {
	      Files.delete(p);
	      System.out.println(p + "  deleted successfully.");
	    } catch (NoSuchFileException e) {
	      Messages.addGlobalError("Arquivo não existe." + e);
	    } catch (DirectoryNotEmptyException e) {
	      Messages.addGlobalError("Diretorio" + p +" não está vazio. "+ e);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }

	  }
	
	/**
	 * Deleta arquivos dentro de um diretório
	 * 
	 * @param caminho  - Informar caminho do diretório
	 * @throws Exception
	 */
	public static void delFilesDir(String caminho) throws Exception {
	    try {
	    	File dir = new File(caminho);
			if (dir.isDirectory()) {
				String[] arqs = dir.list();
				for (String nome : arqs) {
					System.out.println(nome);
					Path p = Paths.get(caminho+nome);
					Files.delete(p);
				    System.out.println(">>>>>> Arquivo deletado com sucesso >>>>>>>");
				}
			}
	    } catch (NoSuchFileException e) {
	      Messages.addGlobalError("Arquivo não existe." + e);
	    } catch (DirectoryNotEmptyException e) {
	      Messages.addGlobalError("Diretorio" + caminho +" não está vazio. "+ e);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }

	  }

	/**
	 * Metodo para renomear arquivos ou diretorios
	 * OBS: pode ser usado tanto para aquivos quanto para diretorios
	 * @param caminhoOrigem
	 * @param caminhoDestino
	 */
	public static void renomeia(String caminhoOrigem, String caminhoDestino) {
		File file1 = new File(caminhoOrigem); // arquivo ou diretorio
		File file2 = new File(caminhoDestino);

		boolean sucesso = file1.renameTo(file2);
		if (sucesso) {
//			Messages.addGlobalInfo("Arquivo renomeado com sucesso.");
			System.out.println("Arquivo Renomeado com sucesso.");
		}
	}
}
