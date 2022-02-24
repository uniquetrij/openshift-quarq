import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuarqSharedModule } from 'app/shared/shared.module';
import { EmbeddingComponent } from './embedding.component';
import { EmbeddingDetailComponent } from './embedding-detail.component';
import { EmbeddingUpdateComponent } from './embedding-update.component';
import { EmbeddingDeleteDialogComponent } from './embedding-delete-dialog.component';
import { embeddingRoute } from './embedding.route';

@NgModule({
  imports: [QuarqSharedModule, RouterModule.forChild(embeddingRoute)],
  declarations: [EmbeddingComponent, EmbeddingDetailComponent, EmbeddingUpdateComponent, EmbeddingDeleteDialogComponent],
  entryComponents: [EmbeddingDeleteDialogComponent],
})
export class QuarqEmbeddingModule {}
