import {
  HandleRFPFileRemove,
  HandleRFPFileSelectionError,
  HandleRFPFileUploaded,
} from "./datalayer";

import { RFPService } from "./digital-form-submit";
class FileUploadControl {
  /**
   * @param {HTMLDivElement} root file upload container div
   * @param {HTMLFormElement} form the form
   */
  constructor(root, form) {
    /**@type {File[]} */
    this.form = form;
    this.uploadedFiles = form._uploadedFiles = [];
    this.FormConfigs = { ...form.dataset }; //other rfp endpoints
    this.ui = this.getUI(root);
    this.attachEvents();
  }
  getUI(/**@type {HTMLDivElement} */ root) {
    return {
      /**@type {HTMLDivElement} */
      root,
      /**@type {HTMLInputElement} */
      fileInput: root.querySelector('input[type="file"]'),
      /**@type {HTMLDivElement} */
      uploadContainer: root.querySelector("#file-upload-container"),
      /**@type {HTMLUListElement} */
      fileList: root.querySelector("#file-list"),
      /**@type {HTMLDivElement} */
      errorMessage: root.querySelector(".err-msg"),
    };
  }
  attachEvents() {
    this._onDragOver = this._onDragOver.bind(this);
    this._onDragLeave = this._onDragLeave.bind(this);
    this._onDrop = this._onDrop.bind(this);

    this.ui.fileInput.addEventListener("change", (e) => {
      e.target.setCustomValidity("");
      this.haveFiles(e.target.files);
    });

    this.ui.uploadContainer.addEventListener("click", () =>
      this.ui.fileInput.click()
    );
    this.ui.uploadContainer.addEventListener("dragover", this._onDragOver);
    this.ui.uploadContainer.addEventListener("dragleave", this._onDragLeave);
    this.ui.uploadContainer.addEventListener("drop", this._onDrop);
    this._handleRequiredFieldValidity();
  }

  _handleRequiredFieldValidity() {
    if (!this.ui.fileInput.required) {
      return;
    }
    this.ui.fileInput.addEventListener("invalid", (e) => {
      e.target.setCustomValidity("");
      e.preventDefault(); //no default tooltip
      this.ui.uploadContainer.classList.add("invalid");
      if (e.target.validity.valueMissing) {
        this.showErrorMessage(
          this.ui.uploadContainer.dataset.requiredMessage || ""
        );
      }
    });
  }

  _onDragOver(evnt) {
    evnt.preventDefault();
    this.ui.uploadContainer.classList.add("drag-over");
  }
  _onDragLeave(evnt) {
    evnt.preventDefault();
    this.ui.uploadContainer.classList.remove("drag-over");
  }
  _onDrop(evnt) {
    evnt.preventDefault();
    this.ui.uploadContainer.classList.remove("drag-over");
    this.haveFiles(evnt.dataTransfer.files);
  }
  _isValidFile(file) {
    const MAX_FILES = this.ui.uploadContainer.dataset.maxFiles || 5,
      MAX_SIZE_MB = this.ui.uploadContainer.dataset.maxFileSize || 15;
    if (this.uploadedFiles.length >= MAX_FILES) {
      HandleRFPFileSelectionError("|file number");
      this.showErrorMessage(
        this.ui.uploadContainer.dataset.maxFilesError ||
          "Maximum number of files reached!"
      ); //config
      return;
    }

    if (this.isDuplicateFile(file)) {
      HandleRFPFileSelectionError("|duplicate");
      this.showErrorMessage(
        this.ui.uploadContainer.dataset.duplicateError ||
          "No duplicates allowed."
      );
      return false;
    }

    if (!this.isSupportedFileType(file)) {
      HandleRFPFileSelectionError("|file type");
      this.showErrorMessage(
        this.ui.uploadContainer.dataset.invalidTypeError ||
          "Invalid file! Supported file types: .doc, .docx, .mpp," +
            ".mppx, .pdf, .ppt, .pptx, .xls, .xlsx, .zip, .msg."
      );
      return false;
    }
    let totalSize = this.uploadedFiles.reduce(
      (prevSize, { size: currSize }) => prevSize + currSize,
      file.size
    );
    // const fileSizeMB = file.size / (1024 * 1024);
    const fileSizeMB = totalSize / (1024 * 1024);
    if (fileSizeMB > MAX_SIZE_MB) {
      HandleRFPFileSelectionError("|file size");
      this.showErrorMessage(
        this.ui.uploadContainer.dataset.maxSizeError ||
          `File size exceeds the limit of ${MAX_SIZE_MB}MB.`
      );
      return false;
    }
    this.hideErrorMessage();
    return true;
  }
  haveFiles(files) {
    this.hideErrorMessage();
    for (const file of files) {
      let isvalid = this._isValidFile(file);
      if (isvalid === false) {
        continue;
      } //invalid
      else if (!isvalid) {
        return;
      } //max files reached
      else {
        this._addAFile(file);
      } //valid
    }
  }
  showErrorMessage(msg) {
    //clear
    this.ui.uploadContainer.classList.add("invalid");
    let msgArea = this.ui.errorMessage;
    let span = msgArea.querySelector("span");
    if (!span) {
      span = document.createElement("span");
      span.className = "prepend-icon-info";
      msgArea.appendChild(span);
    }

    //clear
    while (span.firstChild) {
      span.removeChild(span.lastChild);
    }

    //update msg
    span.appendChild(document.createTextNode(msg));

    msgArea.classList.add("show");
    msgArea.setAttribute("aria-hidden", false);
  }
  hideErrorMessage() {
    this.ui.uploadContainer.classList.remove("invalid");
    let msgArea = this.ui.errorMessage;
    if (!msgArea) {
      return;
    }
    msgArea.setAttribute("aria-hidden", true);
    msgArea.classList.remove("show");
  }
  _addAFile(file) {
    this.uploadedFiles.push(file);
    const randomID = `uploaded-file-${Math.floor(Math.random() * 100000)}`;
    RFPService.handleFileUpload(file, this.form, randomID);
    try {
      HandleRFPFileUploaded(this.uploadedFiles);
    } catch (error) {
      console.warn(error);
    }
    const listItem = this._getNewFileUI(file, randomID);
    this.ui.fileList.appendChild(listItem);
  }
  _getNewFileUI(file, randomID) {
    const listItem = document.createElement("li");
    const fileIcon = document.createElement("span");
    const fileName = document.createElement("span");
    const cancelButton = document.createElement("button");
    const loadingIcon = document.createElement("span");
    //todo:use translated value
    fileIcon.appendChild(document.createTextNode("description"));

    fileIcon.classList.add("file-upload__icon");
    fileName.appendChild(document.createTextNode(file.name));

    fileName.classList.add("file-upload__file-name");
    loadingIcon.classList.add("file-upload__loading");
    cancelButton.setAttribute("type", "button");
    cancelButton.appendChild(document.createTextNode("clear")); //icon name
    cancelButton.classList.add("file-upload__cancel-button");
    listItem.setAttribute("id", randomID);
    listItem.appendChild(fileIcon);
    listItem.appendChild(fileName);
    listItem.appendChild(loadingIcon);
    listItem.appendChild(cancelButton);

    cancelButton.addEventListener("click", () => {
      this._onRemoveFileClick(file, randomID);
    });

    return listItem;
  }
  _onRemoveFileClick(file, randomID) {
    this.hideErrorMessage();
    //removes all matching files
    for (let i = this.uploadedFiles.length - 1; i >= 0; i--) {
      const element = this.uploadedFiles[i];
      if (element.name == file.name && element.size == file.size) {
        this.uploadedFiles.splice(i, 1);
      }
    }
    RFPService.handleFileRemove(file, this.form, randomID);
    HandleRFPFileRemove(this.uploadedFiles);
  }
  isSupportedFileType(file) {
    const allowedExtensions = this.ui.uploadContainer.dataset.allowedExtensions
      ? this.ui.uploadContainer.dataset.allowedExtensions.split()
      : [
          ".doc",
          ".docx",
          ".mpp",
          ".mppx",
          ".pdf",
          ".ppt",
          ".pptx",
          ".xls",
          ".xlsx",
          ".zip",
          ".msg",
        ];
    const fileName = file.name;
    const fileExtension = fileName
      .substring(fileName.lastIndexOf("."))
      .toLowerCase();
    return allowedExtensions.includes(fileExtension);
  }
  isDuplicateFile(file) {
    return this.uploadedFiles.some(
      (uploadedFile) =>
        uploadedFile.name === file.name && uploadedFile.size === file.size
    );
  }
}

/**@type {FileUploadControl[]} */
const instances = [];
/**
 * @param {HTMLFormElement} form
 * @returns {FileUploadControl[]}
 */
export function InstantiateFileUpload(form) {
  let fileFields = form.querySelectorAll(".fileupload");
  fileFields.forEach((element) => {
    let instance = new FileUploadControl(element, form);
    instances.push(instance);
  });
  return instances;
}
