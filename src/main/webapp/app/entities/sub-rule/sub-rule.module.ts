import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SubRuleComponent } from './list/sub-rule.component';
import { SubRuleDetailComponent } from './detail/sub-rule-detail.component';
import { SubRuleUpdateComponent } from './update/sub-rule-update.component';
import { SubRuleDeleteDialogComponent } from './delete/sub-rule-delete-dialog.component';
import { SubRuleRoutingModule } from './route/sub-rule-routing.module';

@NgModule({
  imports: [SharedModule, SubRuleRoutingModule],
  declarations: [SubRuleComponent, SubRuleDetailComponent, SubRuleUpdateComponent, SubRuleDeleteDialogComponent],
  entryComponents: [SubRuleDeleteDialogComponent],
})
export class SubRuleModule {}
