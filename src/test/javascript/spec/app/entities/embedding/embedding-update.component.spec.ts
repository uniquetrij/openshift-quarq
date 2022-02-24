import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { QuarqTestModule } from '../../../test.module';
import { EmbeddingUpdateComponent } from 'app/entities/embedding/embedding-update.component';
import { EmbeddingService } from 'app/entities/embedding/embedding.service';
import { Embedding } from 'app/shared/model/embedding.model';

describe('Component Tests', () => {
  describe('Embedding Management Update Component', () => {
    let comp: EmbeddingUpdateComponent;
    let fixture: ComponentFixture<EmbeddingUpdateComponent>;
    let service: EmbeddingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [QuarqTestModule],
        declarations: [EmbeddingUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EmbeddingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmbeddingUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmbeddingService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Embedding(123);
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
        const entity = new Embedding();
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
