import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IEmbedding, Embedding } from 'app/shared/model/embedding.model';
import { EmbeddingService } from './embedding.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';

@Component({
  selector: 'jhi-embedding-update',
  templateUrl: './embedding-update.component.html',
})
export class EmbeddingUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    embedding: [],
    embeddingContentType: [],
    employees: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected embeddingService: EmbeddingService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ embedding }) => {
      this.updateForm(embedding);

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(embedding: IEmbedding): void {
    this.editForm.patchValue({
      id: embedding.id,
      embedding: embedding.embedding,
      embeddingContentType: embedding.embeddingContentType,
      employees: embedding.employees,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('quarqApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const embedding = this.createFromForm();
    if (embedding.id !== undefined) {
      this.subscribeToSaveResponse(this.embeddingService.update(embedding));
    } else {
      this.subscribeToSaveResponse(this.embeddingService.create(embedding));
    }
  }

  private createFromForm(): IEmbedding {
    return {
      ...new Embedding(),
      id: this.editForm.get(['id'])!.value,
      embeddingContentType: this.editForm.get(['embeddingContentType'])!.value,
      embedding: this.editForm.get(['embedding'])!.value,
      employees: this.editForm.get(['employees'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmbedding>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  getSelected(selectedVals: IEmployee[], option: IEmployee): IEmployee {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
