import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { QuarqTestModule } from '../../../test.module';
import { EmbeddingDetailComponent } from 'app/entities/embedding/embedding-detail.component';
import { Embedding } from 'app/shared/model/embedding.model';

describe('Component Tests', () => {
  describe('Embedding Management Detail Component', () => {
    let comp: EmbeddingDetailComponent;
    let fixture: ComponentFixture<EmbeddingDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ embedding: new Embedding(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [QuarqTestModule],
        declarations: [EmbeddingDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EmbeddingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmbeddingDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load embedding on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.embedding).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
