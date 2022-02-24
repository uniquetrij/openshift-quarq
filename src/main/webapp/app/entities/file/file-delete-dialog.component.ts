import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFile } from 'app/shared/model/file.model';
import { FileService } from './file.service';

@Component({
  templateUrl: './file-delete-dialog.component.html',
})
export class FileDeleteDialogComponent {
  file?: IFile;

  constructor(protected fileService: FileService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fileListModification');
      this.activeModal.close();
    });
  }
}
