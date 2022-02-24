import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmbedding } from 'app/shared/model/embedding.model';
import { EmbeddingService } from './embedding.service';

@Component({
  templateUrl: './embedding-delete-dialog.component.html',
})
export class EmbeddingDeleteDialogComponent {
  embedding?: IEmbedding;

  constructor(protected embeddingService: EmbeddingService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.embeddingService.delete(id).subscribe(() => {
      this.eventManager.broadcast('embeddingListModification');
      this.activeModal.close();
    });
  }
}
