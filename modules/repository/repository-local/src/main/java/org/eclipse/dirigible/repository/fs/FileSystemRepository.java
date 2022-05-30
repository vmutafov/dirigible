/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.repository.fs;

import static org.apache.commons.io.IOCase.INSENSITIVE;
import static org.apache.commons.io.IOCase.SENSITIVE;
import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.eclipse.dirigible.commons.api.helpers.FileSystemUtils;
import org.eclipse.dirigible.repository.api.ICollection;
import org.eclipse.dirigible.repository.api.IEntity;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.api.IResource;
import org.eclipse.dirigible.repository.api.RepositoryExportException;
import org.eclipse.dirigible.repository.api.RepositoryImportException;
import org.eclipse.dirigible.repository.api.RepositoryPath;
import org.eclipse.dirigible.repository.api.RepositoryReadException;
import org.eclipse.dirigible.repository.api.RepositorySearchException;
import org.eclipse.dirigible.repository.api.RepositoryWriteException;
import org.eclipse.dirigible.repository.local.LocalCollection;
import org.eclipse.dirigible.repository.local.LocalRepositoryDao;
import org.eclipse.dirigible.repository.local.LocalRepositoryException;
import org.eclipse.dirigible.repository.local.LocalResource;
import org.eclipse.dirigible.repository.local.LocalWorkspaceMapper;
import org.eclipse.dirigible.repository.search.RepositorySearcher;
import org.eclipse.dirigible.repository.zip.RepositoryZipExporter;
import org.eclipse.dirigible.repository.zip.RepositoryZipImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The File System based implementation of {@link IRepository}.
 */
public abstract class FileSystemRepository implements IRepository {

	private static final String REPOSITORY_FILE_BASED = "REPOSITORY_FILE_BASED";
	
	private static final String REPOSITORY_ROOT_FOLDER = "REPOSITORY_ROOT_FOLDER";

	private static final String REPOSITORY_INDEX_FOLDER = "REPOSITORY_INDEX_FOLDER";

	private static Logger logger = LoggerFactory.getLogger(FileSystemRepository.class);

	private static final String CURRENT_DIR = ".";

	private static final String DIRIGIBLE_LOCAL = "dirigible" + IRepository.SEPARATOR + "repository";

	private static final String PATH_SEGMENT_ROOT = "root";

	private static final String DIRIGIBLE_LOCAL_ROOT = DIRIGIBLE_LOCAL + IRepository.SEPARATOR + PATH_SEGMENT_ROOT;

	private String repositoryPath = IRepository.SEPARATOR;

	private LocalRepositoryDao repositoryDao;

	private RepositorySearcher repositorySearcher;
	
	private Map<String, String> parameters = Collections.synchronizedMap(new HashMap<>());

	/**
	 * Constructor with default root folder - user.dir and without database initialization
	 *
	 * @throws LocalRepositoryException
	 *             in case the repository cannot be created
	 */
	public FileSystemRepository() throws LocalRepositoryException {
		createRepository(null, false);
	}

	/**
	 * Constructor with root folder parameter.
	 *
	 * @param rootFolder
	 *            the root folder
	 * @throws LocalRepositoryException
	 *             in case the repository cannot be created
	 */
	public FileSystemRepository(String rootFolder) throws LocalRepositoryException {
		createRepository(rootFolder, false);
	}

	/**
	 * Constructor with root folder parameter.
	 *
	 * @param rootFolder
	 *            the root folder
	 * @param absolute
	 *            whether the root folder is absolute
	 * @throws LocalRepositoryException
	 *             in case the repository cannot be created
	 */
	public FileSystemRepository(String rootFolder, boolean absolute) throws LocalRepositoryException {
		createRepository(rootFolder, absolute);
	}

	/**
	 * Creates the repository.
	 *
	 * @param rootFolder
	 *            the root folder
	 * @param absolute
	 *            the absolute
	 */
	protected void createRepository(String rootFolder, boolean absolute) {
		String root;
		if (absolute) {
			if (rootFolder != null) {
				root = rootFolder;
			} else {
				throw new LocalRepositoryException("Creating a FileSystemRepository with absolute path flag, but the path itself is null");
			}
		} else {
			root = System.getProperty("user.dir");
			if ((rootFolder != null) && !rootFolder.equals(CURRENT_DIR)) {
				root += File.separator;
				root += rootFolder;
			}
		}
		this.repositoryDao = new LocalRepositoryDao(this);

		logger.debug(String.format("Creating File-based Repository Client for: %s ...", root));
		try {
			initializeRepository(root);
			this.repositorySearcher = new RepositorySearcher(this);
			this.setParameter(REPOSITORY_ROOT_FOLDER, this.repositorySearcher.getRoot() + IRepository.SEPARATOR + DIRIGIBLE_LOCAL_ROOT);
			this.setParameter(REPOSITORY_INDEX_FOLDER, this.repositorySearcher.getRoot());
		} catch (IOException e) {
			throw new LocalRepositoryException();
		}
		this.setParameter(REPOSITORY_FILE_BASED, "true");
		logger.debug(String.format("File-based Repository Client for: %s, has been created.", root));
	}

	/**
	 * Gets the repository path.
	 *
	 * @return the repository path
	 */
	@Override
	public String getRepositoryPath() {
		return repositoryPath;
	}

	/**
	 * Gets the repository root folder.
	 *
	 * @return the repository root folder
	 */
	protected String getRepositoryRootFolder() {
		return DIRIGIBLE_LOCAL;
	}

	/**
	 * Initialize repository.
	 *
	 * @param rootFolder
	 *            the root folder
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void initializeRepository(String rootFolder) throws IOException {
		repositoryPath = rootFolder + IRepository.SEPARATOR + getRepositoryRootFolder() + IRepository.SEPARATOR + PATH_SEGMENT_ROOT; // $NON-NLS-1$
		repositoryPath = repositoryPath.replace(IRepository.SEPARATOR, File.separator);
		repositoryPath = new File(repositoryPath).getAbsolutePath();
		FileSystemUtils.createFolder(repositoryPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryReader#getRoot()
	 */
	@Override
	public ICollection getRoot() {
		logger.trace("entering getRoot"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(IRepository.SEPARATOR);
		LocalCollection localCollection = new LocalCollection(this, wrapperPath);
		logger.trace("exiting getRoot"); //$NON-NLS-1$
		return localCollection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#createCollection(java.lang.String)
	 */
	@Override
	public ICollection createCollection(String path) throws RepositoryWriteException {
		logger.trace("entering createCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final LocalCollection localCollection = new LocalCollection(this, wrapperPath);
		localCollection.create();
		logger.trace("exiting createCollection"); //$NON-NLS-1$
		return localCollection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryReader#getCollection(java.lang.String)
	 */
	@Override
	public ICollection getCollection(String path) {
		logger.trace("entering getCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		LocalCollection localCollection = new LocalCollection(this, wrapperPath);
		logger.trace("exiting getCollection"); //$NON-NLS-1$
		return localCollection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#removeCollection(java.lang.String)
	 */
	@Override
	public void removeCollection(String path) throws RepositoryWriteException {
		logger.trace("entering removeCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final ICollection collection = new LocalCollection(this, wrapperPath);
		collection.delete();
		logger.trace("exiting removeCollection"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryReader#hasCollection(java.lang.String)
	 */
	@Override
	public boolean hasCollection(String path) throws RepositoryReadException {
		logger.trace("entering hasCollection"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final ICollection collection = new LocalCollection(this, wrapperPath);
		boolean result = collection.exists();
		logger.trace("exiting hasCollection"); //$NON-NLS-1$
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#createResource(java.lang.String)
	 */
	@Override
	public IResource createResource(String path) throws RepositoryWriteException {
		logger.trace("entering createResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new LocalResource(this, wrapperPath);
		resource.create();
		logger.trace("exiting createResource"); //$NON-NLS-1$
		return resource;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#createResource(java.lang.String, byte[])
	 */
	@Override
	public IResource createResource(String path, byte[] content) throws RepositoryWriteException {
		logger.trace("entering createResource with Content"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new LocalResource(this, wrapperPath);
		resource.setContent(content);
		logger.trace("exiting createResource with Content"); //$NON-NLS-1$
		return resource;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#createResource(java.lang.String, byte[], boolean,
	 * java.lang.String)
	 */
	@Override
	public IResource createResource(String path, byte[] content, boolean isBinary, String contentType) throws RepositoryWriteException {
		return createResource(path, content, isBinary, contentType, false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#createResource(java.lang.String, byte[], boolean,
	 * java.lang.String, boolean)
	 */
	@Override
	public IResource createResource(String path, byte[] content, boolean isBinary, String contentType, boolean override)
			throws RepositoryWriteException {
		logger.trace("entering createResource with Content"); //$NON-NLS-1$
		try {
			final RepositoryPath wrapperPath = new RepositoryPath(path);
			getRepositoryDao().createFile(wrapperPath.toString(), content, isBinary, contentType);
		} catch (LocalRepositoryException e) {
			throw new RepositoryWriteException(e);
		}
		final IResource resource = getResource(path);
		logger.trace("exiting createResource with Content"); //$NON-NLS-1$
		return resource;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryReader#getResource(java.lang.String)
	 */
	@Override
	public IResource getResource(String path) {
		logger.trace("entering getResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		LocalResource resource = new LocalResource(this, wrapperPath);
		logger.trace("exiting getResource"); //$NON-NLS-1$
		return resource;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#removeResource(java.lang.String)
	 */
	@Override
	public void removeResource(String path) throws RepositoryWriteException {
		logger.trace("entering removeResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new LocalResource(this, wrapperPath);
		resource.delete();
		logger.trace("exiting removeResource"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryReader#hasResource(java.lang.String)
	 */
	@Override
	public boolean hasResource(String path) throws RepositoryReadException {
		logger.trace("entering hasResource"); //$NON-NLS-1$
		final RepositoryPath wrapperPath = new RepositoryPath(path);
		final IResource resource = new LocalResource(this, wrapperPath);
		boolean result = resource.exists();
		logger.trace("exiting hasResource"); //$NON-NLS-1$
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryWriter#dispose()
	 */
	@Override
	public void dispose() {
		// repositoryDAO.dispose();
	}

	/**
	 * Gets the repository dao.
	 *
	 * @return the repository dao
	 */
	public LocalRepositoryDao getRepositoryDao() {
		return repositoryDao;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryImporter#importZip(java.util.zip.ZipInputStream,
	 * java.lang.String)
	 */
	@Override
	public void importZip(ZipInputStream zipInputStream, String path) throws RepositoryImportException {
		importZip(zipInputStream, path, false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryImporter#importZip(java.util.zip.ZipInputStream,
	 * java.lang.String, boolean)
	 */
	@Override
	public void importZip(ZipInputStream zipInputStream, String path, boolean override) throws RepositoryImportException {
		importZip(zipInputStream, path, override, false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryImporter#importZip(java.util.zip.ZipInputStream,
	 * java.lang.String, boolean, boolean)
	 */
	@Override
	public void importZip(ZipInputStream zipInputStream, String relativeRoot, boolean override, boolean excludeRootFolderName)
			throws RepositoryImportException {
		if (zipInputStream == null) {
			logger.error("Provided Zip Input Stream cannot be null");
			throw new RepositoryImportException("Provided Zip Input Stream cannot be null");
		}
		RepositoryZipImporter.importZip(this, zipInputStream, relativeRoot, override, excludeRootFolderName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryImporter#importZip(byte[], java.lang.String)
	 */
	@Override
	public void importZip(byte[] data, String path) throws RepositoryImportException {
		importZip(data, path, false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryImporter#importZip(byte[], java.lang.String, boolean)
	 */
	@Override
	public void importZip(byte[] data, String path, boolean override) throws RepositoryImportException {
		importZip(data, path, override, false, null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryImporter#importZip(byte[], java.lang.String, boolean,
	 * boolean, java.util.Map)
	 */
	@Override
	public void importZip(byte[] data, String relativeRoot, boolean override, boolean excludeRootFolderName, Map<String, String> filter)
			throws RepositoryImportException {
		if (data == null) {
			logger.error("Provided Zip Data cannot be null");
			throw new RepositoryImportException("Provided Zip Data cannot be null");
		}
		RepositoryZipImporter.importZip(this, new ZipInputStream(new ByteArrayInputStream(data)), relativeRoot, override, excludeRootFolderName,
				filter);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryExporter#exportZip(java.util.List)
	 */
	@Override
	public byte[] exportZip(List<String> relativeRoots) throws RepositoryExportException {
		return RepositoryZipExporter.exportZip(this, relativeRoots);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryExporter#exportZip(java.lang.String, boolean)
	 */
	@Override
	public byte[] exportZip(String relativeRoot, boolean inclusive) throws RepositoryExportException {
		return RepositoryZipExporter.exportZip(this, relativeRoot, inclusive);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositorySearch#searchName(java.lang.String, boolean)
	 */
	@Override
	public List<IEntity> searchName(String parameter, boolean caseInsensitive) throws RepositorySearchException {
		return searchName(IRepository.SEPARATOR, parameter, caseInsensitive);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositorySearch#searchName(java.lang.String, java.lang.String,
	 * boolean)
	 */
	@Override
	public List<IEntity> searchName(String root, String parameter, boolean caseInsensitive) throws RepositorySearchException {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(this, root);

			List<IEntity> entities = new ArrayList<IEntity>();

			if ((parameter == null) || "".equals(parameter)) {
				return entities;
			}

			String rootRepositoryPath = getRepositoryPath();
			File dir = new File(workspacePath);
			Iterator<File> foundFiles = FileUtils.iterateFiles(dir,
					new WildcardFileFilter("*" + parameter + "*", (caseInsensitive ? INSENSITIVE : SENSITIVE)), TRUE);
			while (foundFiles.hasNext()) {
				File foundFile = foundFiles.next();
				String foundFilePath = foundFile.getAbsolutePath();
				if (foundFilePath.length() <= rootRepositoryPath.length()) {
					throw new RepositorySearchException(String.format("The found file name [%s] is shorter than the repository root file name [%s]",
							foundFilePath, rootRepositoryPath));
				}
				String repositoryName = foundFilePath.substring(rootRepositoryPath.length());
				RepositoryPath localRepositoryPath = new RepositoryPath(repositoryName);
				entities.add(new LocalResource(this, localRepositoryPath));
			}

			return entities;
		} catch (RepositoryWriteException e) {
			throw new RepositorySearchException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositorySearch#searchPath(java.lang.String, boolean)
	 */
	@Override
	public List<IEntity> searchPath(String parameter, boolean caseInsensitive) throws RepositorySearchException {
		String rootRepositoryPath = getRepositoryPath();
		List<IEntity> entities = new ArrayList<IEntity>();
		Iterator<File> foundFiles = FileUtils.iterateFiles(new File(rootRepositoryPath),
				new WildcardFileFilter("*" + parameter + "*", (caseInsensitive ? INSENSITIVE : SENSITIVE)), TRUE);
		while (foundFiles.hasNext()) {
			File foundFile = foundFiles.next();
			String repositoryName = foundFile.getAbsolutePath().substring(getRepositoryPath().length());
			RepositoryPath localRepositoryPath = new RepositoryPath(repositoryName);
			entities.add(new LocalResource(this, localRepositoryPath));
		}

		return entities;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositorySearch#searchText(java.lang.String, boolean)
	 */
	@Override
	public List<IEntity> searchText(String parameter) throws RepositorySearchException {
		List<IEntity> entities = new ArrayList<IEntity>();
		List<String> paths = repositorySearcher.search(parameter);
		for (String path : paths) {
			entities.add(new LocalResource(this, new RepositoryPath(path)));
		}
		return entities;
	}

	@Override
	public void searchRefresh() throws RepositorySearchException {
		repositorySearcher.forceReindex();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositoryReader#getAllResourcePaths()
	 */
	@Override
	public List<String> getAllResourcePaths() throws RepositoryReadException {
		String rootRepositoryPath = getRepositoryPath();
		List<String> paths = new ArrayList<String>();
		Iterator<File> foundFiles = FileUtils.iterateFiles(new File(rootRepositoryPath), new WildcardFileFilter("*.*", INSENSITIVE), TRUE);
		while (foundFiles.hasNext()) {
			File foundFile = foundFiles.next();
			String repositoryName = foundFile.getAbsolutePath().substring(getRepositoryPath().length());
			RepositoryPath localRepositoryPath = new RepositoryPath(repositoryName);
			paths.add(localRepositoryPath.toString());
		}
		return paths;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepository#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}
	
	protected void setParameter(String key, String value) {
		parameters.put(key, value);
	}
	
	@Override
	public boolean isLinkingPathsSupported() {
		return true;
	}
	
	@Override
	public void linkPath(String repositoryPath, String filePath) throws IOException {
		if (!new File(filePath).exists()) {
			throw new IOException("The source path does not exist: " + filePath);
		}
		String workspacePath = LocalWorkspaceMapper.getMappedName(this, repositoryPath);
		if (!Paths.get(workspacePath).getParent().toFile().exists()) {
			FileSystemUtils.forceCreateDirectory(Paths.get(workspacePath).getParent().toString());
		}
		Files.createSymbolicLink(Paths.get(workspacePath).toAbsolutePath(), Paths.get(filePath).toAbsolutePath());
	}

	@Override
	public void deleteLinkedPath(String repositoryPath) throws IOException {
		if (isLinkedPath(repositoryPath)) {
			String workspacePath = LocalWorkspaceMapper.getMappedName(this, repositoryPath);
			Path filePath = Paths.get(workspacePath);
			Files.delete(filePath);
		}
	}

	@Override
	public boolean isLinkedPath(String repositoryPath) {
		String workspacePath = LocalWorkspaceMapper.getMappedName(this, repositoryPath);
		return Files.isSymbolicLink(Paths.get(workspacePath).toAbsolutePath());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.repository.api.IRepositorySearch#find(java.lang.String, boolean)
	 */
	@Override
	public List<String> find(String path, String pattern) throws RepositorySearchException {
		try {
			ICollection collection = getCollection(path);
			if (collection.exists() && collection instanceof LocalCollection) {
				List<String> list = FileSystemUtils.find(((LocalCollection) collection).getFolder().getPath(), pattern);
				int repositoryRootLength = ((LocalCollection) collection.getRepository().getRoot()).getFolder().getPath().length();
				List<String> prepared = new ArrayList<String>();
				list.forEach(item -> {
						String truncated = item.substring(repositoryRootLength);
						if (!IRepository.SEPARATOR.equals(File.separator)) {
							truncated = truncated.replace(File.separator, IRepository.SEPARATOR);
						}
						prepared.add(truncated);
					});
				return prepared;
			}
		} catch (RepositoryReadException | IOException e) {
			throw new RepositorySearchException(e);
		}
		return new ArrayList<String>();
	}

	@Override
	public String getInternalResourcePath(String resourcePath) {
		return LocalWorkspaceMapper.getMappedName(this, resourcePath);
	}

}
