import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { QuarqTestModule } from '../../../test.module';
import { FileUpdateComponent } from 'app/entities/file/file-update.component';
import { FileService } from 'app/entities/file/file.service';
import { File } from 'app/shared/model/file.model';

describe('Component Tests', () => {
  describe('File Management Update Component', () => {
    let comp: FileUpdateComponent;
    let fixture: ComponentFixture<FileUpdateComponent>;
    let service: FileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [QuarqTestModule],
        declarations: [FileUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new File(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new File();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
