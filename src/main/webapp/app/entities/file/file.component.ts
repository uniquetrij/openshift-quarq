import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFile } from 'app/shared/model/file.model';
import { FileService } from './file.service';
import { FileDeleteDialogComponent } from './file-delete-dialog.component';

@Component({
  selector: 'jhi-file',
  templateUrl: './file.component.html',
})
export class FileComponent implements OnInit, OnDestroy {
  files?: IFile[];
  eventSubscriber?: Subscription;

  constructor(protected fileService: FileService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.fileService.query().subscribe((res: HttpResponse<IFile[]>) => (this.files = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFiles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFile): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFiles(): void {
    this.eventSubscriber = this.eventManager.subscribe('fileListModification', () => this.loadAll());
  }

  delete(file: IFile): void {
    const modalRef = this.modalService.open(FileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.file = file;
  }
}
