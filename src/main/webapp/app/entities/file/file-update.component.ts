import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFile, File } from 'app/shared/model/file.model';
import { FileService } from './file.service';
import { IEmployee } from 'app/shared/model/employee.model';
import { EmployeeService } from 'app/entities/employee/employee.service';

@Component({
  selector: 'jhi-file-update',
  templateUrl: './file-update.component.html',
})
export class FileUpdateComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    identifier: [],
    location: [],
    uri: [],
    encryption: [],
    encryptionAlgorithm: [],
    employees: [],
  });

  constructor(
    protected fileService: FileService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ file }) => {
      this.updateForm(file);

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    });
  }

  updateForm(file: IFile): void {
    this.editForm.patchValue({
      id: file.id,
      identifier: file.identifier,
      location: file.location,
      uri: file.uri,
      encryption: file.encryption,
      encryptionAlgorithm: file.encryptionAlgorithm,
      employees: file.employees,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const file = this.createFromForm();
    if (file.id !== undefined) {
      this.subscribeToSaveResponse(this.fileService.update(file));
    } else {
      this.subscribeToSaveResponse(this.fileService.create(file));
    }
  }

  private createFromForm(): IFile {
    return {
      ...new File(),
      id: this.editForm.get(['id'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      location: this.editForm.get(['location'])!.value,
      uri: this.editForm.get(['uri'])!.value,
      encryption: this.editForm.get(['encryption'])!.value,
      encryptionAlgorithm: this.editForm.get(['encryptionAlgorithm'])!.value,
      employees: this.editForm.get(['employees'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFile>>): void {
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
